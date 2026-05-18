import http from 'k6/http';
import { Counter } from 'k6/metrics';

const immediateCounter = new Counter('immediate_users');
const waitingCounter = new Counter('waiting_users');

export const options = {
    scenarios: {
        spike_test: {
            executor: 'constant-arrival-rate',

            rate: 200,
            timeUnit: '1s',

            duration: '1s',

            preAllocatedVUs: 200,
            maxVUs: 300,
        },
    },
};

export default function () {

    const userId = __VU;

    const res = http.post(
        `http://localhost:8080/api/queue/11/enter?userId=${userId}`,
        null,
        null
    );

    console.log("STATUS:", res.status);
    console.log("BODY:", res.body);

    try {

        const body = JSON.parse(res.body);

        if (body.immediate) {
            immediateCounter.add(1);
        } else {
            waitingCounter.add(1);
        }

    } catch (e) {
        console.log("JSON PARSE ERROR");
    }
}
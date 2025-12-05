## Mock API (Backend Simulation using Mockoon + Mockoon CLI)

This project uses **Mockoon** as a fully simulated backend service instead of relying on a real server. The Android application communicates with this mock backend exactly as it would with an actual remote API. This approach ensures clean architecture principles, avoids hardcoded data, and provides a consistent data layer for the entire development team.

---

## 1. Environment File (Mockoon Configuration)

The entire mock backend configuration — routes, data buckets, response templates, and logic — is stored in the following version-controlled file:
./mock-api/StudentCenterApp.json

Storing this file in the repository ensures:

- Every developer uses the same backend setup  
- The instructor can easily run the backend on any machine  
- The behavior of the API is fully reproducible  
- No developer is dependent on another to run the project

---

## 2. Installing Mockoon CLI

Mockoon CLI allows running the mock backend without the GUI. Install it globally:

```bash
npm install -g @mockoon/cli
```
---

## **3. Starting the Mock API**

From the root of the project directory (StudentCenterApp/), start the mock server with:

mockoon-cli start --data ./mock-api/StudentCenterApp.json --port 3000

This command:
	•	Loads the environment configuration
	•	Boots the server at port 3000
	•	Makes all mock endpoints available for the app

The CLI will print route information and confirm that the server is running.


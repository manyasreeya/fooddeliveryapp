# Read error log file

with open("error.log", "r") as file:
    logs = file.read()

print("\n========== AI ERROR ANALYSIS ==========\n")

# Simulated AI analysis

if "CrashLoopBackOff" in logs:
    print("Root Cause:")
    print("Kubernetes pod is repeatedly crashing due to application startup failure.\n")

    print("Why It Happened:")
    print("Application failed to connect to database during startup.\n")

    print("Suggested Fix:")
    print("1. Verify MySQL database is running")
    print("2. Check Kubernetes secrets and environment variables")
    print("3. Restart deployment after fixing DB connectivity\n")

    print("Prevention:")
    print("Add readiness/liveness probes and retry mechanisms.\n")

elif "ImagePullBackOff" in logs:
    print("Root Cause:")
    print("Docker image could not be pulled from registry.\n")

    print("Suggested Fix:")
    print("1. Verify Docker image name")
    print("2. Check Docker Hub credentials")
    print("3. Ensure image exists in repository\n")

else:
    print("No known error pattern detected.\n")

print("=======================================\n")
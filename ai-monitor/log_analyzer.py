import datetime
import os

LOG_FILE = "ai-monitor/error.log"
REPORT_FILE = "ai-monitor/ai-report.txt"

ERROR_PATTERNS = {
    "OOMKilled": {
        "root_cause": "Pod ran out of memory and was killed by Kubernetes",
        "fix": "Increase memory limit in deployment.yaml — set limits.memory to 1Gi",
        "prevention": "Add HPA with memory-based scaling and monitor with Datadog"
    },
    "CrashLoopBackOff": {
        "root_cause": "Container is repeatedly crashing on startup",
        "fix": "Check logs with: kubectl logs <pod> --previous — fix app startup error",
        "prevention": "Add readinessProbe and livenessProbe to deployment.yaml"
    },
    "ImagePullBackOff": {
        "root_cause": "Kubernetes cannot pull the Docker image",
        "fix": "Check Docker Hub credentials and image name in deployment.yaml",
        "prevention": "Use imagePullPolicy: Always and store credentials in K8s secret"
    },
    "ConnectionRefused": {
        "root_cause": "Service cannot connect to database or another service",
        "fix": "Check MySQL service is running: kubectl get svc — verify DB_HOST env variable",
        "prevention": "Use Kubernetes readinessProbe to delay traffic until DB is ready"
    },
    "OutOfMemory": {
        "root_cause": "JVM heap space exhausted",
        "fix": "Add -Xmx512m to JVM args in Dockerfile ENTRYPOINT",
        "prevention": "Monitor heap usage in Datadog — set alert at 80 percent"
    },
    "Pending": {
        "root_cause": "Pod cannot be scheduled — insufficient cluster resources",
        "fix": "Scale up node group in EKS console or reduce resource requests",
        "prevention": "Enable Cluster Autoscaler in EKS"
    }
}

def analyze_logs():
    report_lines = []
    report_lines.append("=" * 60)
    report_lines.append("FUSION CLOUD — AI INCIDENT ANALYSIS REPORT")
    report_lines.append(f"Generated: {datetime.datetime.now()}")
    report_lines.append("=" * 60)

    if not os.path.exists(LOG_FILE):
        report_lines.append("No error log found — system healthy")
        write_report(report_lines)
        return

    with open(LOG_FILE, "r") as f:
        log_content = f.read()

    issues_found = False
    for pattern, analysis in ERROR_PATTERNS.items():
        if pattern.lower() in log_content.lower():
            issues_found = True
            report_lines.append(f"\n[ISSUE DETECTED] {pattern}")
            report_lines.append(f"  Root Cause : {analysis['root_cause']}")
            report_lines.append(f"  Fix        : {analysis['fix']}")
            report_lines.append(f"  Prevention : {analysis['prevention']}")
            report_lines.append("-" * 60)

    if not issues_found:
        report_lines.append("\n[STATUS] No critical issues detected — all systems normal")

    report_lines.append("\n[RECOMMENDATION] Review Datadog dashboard for metrics")
    report_lines.append("[RECOMMENDATION] Check pod status: kubectl get pods -o wide")
    report_lines.append("=" * 60)

    write_report(report_lines)

def write_report(lines):
    report = "\n".join(lines)
    print(report)
    with open(REPORT_FILE, "w") as f:
        f.write(report)
    print(f"\nReport saved to: {REPORT_FILE}")

if __name__ == "__main__":
    analyze_logs()
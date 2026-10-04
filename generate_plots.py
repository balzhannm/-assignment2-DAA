from pathlib import Path
import pandas as pd
import matplotlib.pyplot as plt

ROOT = Path(__file__).parent
CSV = ROOT / "results" / "results.csv"
OUT = ROOT / "results" / "plots"
OUT.mkdir(parents=True, exist_ok=True)

df = pd.read_csv(CSV)

for p in OUT.glob("*.png"):
    p.unlink()

for workload, title in [
    ("W1", "W1 - Random Access"),
    ("W2", "W2 - Search"),
    ("W4", "W4 - Priority Processing"),
]:
    sub = df[df.workload == workload]
    plt.figure(figsize=(8, 5))
    for structure in sub.structure.unique():
        s = sub[sub.structure == structure]
        plt.plot(s.n, s.time_ms, marker="o", label=structure)
    plt.xscale("log")
    plt.yscale("symlog", linthresh=1)
    plt.xlabel("n")
    plt.ylabel("Time (ms)")
    plt.title(f"{title}: Time vs n")
    plt.grid(True, alpha=0.25)
    plt.legend()
    plt.tight_layout()
    plt.savefig(OUT / f"{workload}_time_vs_n.png", dpi=160)
    plt.close()

for variant in ["head", "middle"]:
    sub = df[(df.workload == "W3") & (df.variant == variant)]
    plt.figure(figsize=(8, 5))
    for structure in sub.structure.unique():
        s = sub[sub.structure == structure]
        plt.plot(s.n, s.time_ms, marker="o", label=structure)
    plt.xscale("log")
    plt.yscale("symlog", linthresh=1)
    plt.xlabel("n")
    plt.ylabel("Time (ms)")
    plt.title(f"W3 - {variant}: Time vs n")
    plt.grid(True, alpha=0.25)
    plt.legend()
    plt.tight_layout()
    plt.savefig(OUT / f"W3_{variant}_time_vs_n.png", dpi=160)
    plt.close()

for workload in ["W1", "W2", "W4"]:
    sub = df[df.workload == workload]
    plt.figure(figsize=(8, 5))
    for metric in ["steps", "moves", "comparisons"]:
        for structure in sub.structure.unique():
            s = sub[sub.structure == structure]
            if s[metric].max() > 0:
                plt.plot(s.n, s[metric], marker="o",
                         label=f"{structure} {metric}")
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel("n")
    plt.ylabel("Count")
    plt.title(f"{workload}: Operation counts vs n")
    plt.grid(True, alpha=0.25)
    plt.legend()
    plt.tight_layout()
    plt.savefig(OUT / f"{workload}_metrics_vs_n.png", dpi=160)
    plt.close()

for variant in ["head", "middle"]:
    sub = df[(df.workload == "W3") & (df.variant == variant)]
    plt.figure(figsize=(8, 5))
    for metric in ["steps", "moves", "comparisons"]:
        for structure in sub.structure.unique():
            s = sub[sub.structure == structure]
            if s[metric].max() > 0:
                plt.plot(s.n, s[metric], marker="o",
                         label=f"{structure} {metric}")
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel("n")
    plt.ylabel("Count")
    plt.title(f"W3 {variant}: Operation counts vs n")
    plt.grid(True, alpha=0.25)
    plt.legend()
    plt.tight_layout()
    plt.savefig(OUT / f"W3_{variant}_metrics_vs_n.png", dpi=160)
    plt.close()

print("Plots generated in", OUT)

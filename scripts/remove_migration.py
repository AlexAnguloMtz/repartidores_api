#!/usr/bin/env python3
"""Remove a Flyway migration at any position."""
from pathlib import Path
import argparse
import re

DIR = Path("../src/main/resources/db/migration")
PATTERN = re.compile(r"^V(\d+)__(.+)\.sql$")

def migrations():
    result = []
    for p in DIR.iterdir():
        m = PATTERN.match(p.name)
        if m:
            result.append((int(m.group(1)), p))
    result.sort()
    versions = [v for v, _ in result]
    if versions != list(range(1, len(versions) + 1)):
        raise SystemExit(f"Migration versions must be contiguous from V1. Found: {versions}")
    return result


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("version", type=int, help="Migration version to remove")
    args = parser.parse_args()

    if args.version < 1:
        parser.error("version must be >= 1")
    if not DIR.is_dir():
        raise SystemExit(f"Directory not found: {DIR}")

    items = migrations()
    if not items or args.version > len(items):
        raise SystemExit(f"Version must be between 1 and {len(items)}")

    target = next(path for version, path in items if version == args.version)
    target.unlink()

    # Rename forwards to avoid filename collisions.
    for version, path in items:
        if version > args.version:
            path.rename(DIR / f"V{version - 1}__{path.name.split('__', 1)[1]}")

    print(f"Removed V{args.version}")


if __name__ == "__main__":
    main()

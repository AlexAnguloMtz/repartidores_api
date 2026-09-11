#!/usr/bin/env python3
"""Add a Flyway migration at any position."""
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
    parser.add_argument("position", type=int, help="1-based position")
    parser.add_argument("description", help="Flyway migration description")
    args = parser.parse_args()

    if args.position < 1 or not args.description.strip():
        parser.error("position must be >= 1 and description must not be empty")
    if not DIR.is_dir():
        raise SystemExit(f"Directory not found: {DIR}")

    items = migrations()
    if args.position > len(items) + 1:
        raise SystemExit(f"Position must be between 1 and {len(items) + 1}")

    # Rename backwards to avoid filename collisions.
    for version, path in reversed(items):
        if version >= args.position:
            path.rename(DIR / f"V{version + 1}__{path.name.split('__', 1)[1]}")

    new_path = DIR / f"V{args.position}__{args.description}.sql"
    new_path.touch()
    print(f"Added {new_path}")


if __name__ == "__main__":
    main()

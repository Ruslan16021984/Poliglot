#!/bin/bash

echo "Running exercise generation tests..."
echo ""

./gradlew test --tests "LessonSessionRepositoryTest" --info 2>&1 | grep -A 200 "=== LESSON"

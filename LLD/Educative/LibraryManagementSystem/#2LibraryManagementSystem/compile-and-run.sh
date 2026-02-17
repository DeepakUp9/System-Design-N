#!/bin/bash

# Library Management System - Compile and Run Script
# This script compiles all Java files and runs the demo

echo "=============================================================================="
echo "📚 Library Management System - Compile and Run"
echo "=============================================================================="
echo ""

# Set the source directory
SRC_DIR="src/main/java"
OUTPUT_DIR="out"

# Create output directory if it doesn't exist
mkdir -p "$OUTPUT_DIR"

echo "🔨 Step 1: Compiling Java files..."
echo "------------------------------------------------------------------------------"

# Find and compile all Java files
find "$SRC_DIR" -name "*.java" > sources.txt
javac -d "$OUTPUT_DIR" -sourcepath "$SRC_DIR" @sources.txt

# Check compilation status
if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    rm sources.txt
else
    echo "❌ Compilation failed!"
    rm sources.txt
    exit 1
fi

echo ""
echo "🚀 Step 2: Running Library Management System Demo..."
echo "------------------------------------------------------------------------------"
echo ""

# Run the demo
cd "$OUTPUT_DIR"
java com.library.LibraryManagementSystemDemo
cd ..

echo ""
echo "=============================================================================="
echo "✅ Demo execution completed!"
echo "=============================================================================="

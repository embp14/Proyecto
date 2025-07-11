#!/bin/bash
# Copy all FXML resources from src to build/classes so they are available at runtime
mkdir -p build/classes
rsync -a --include='*/' --include='*.fxml' --exclude='*' src/ build/classes/

#!/bin/bash
set -e

PROJECT_DIR="/home/freyta/git/smw-android/app/jni"
SDL_PATH="$PROJECT_DIR/../SDL2"
SDL_MIXER_PATH="$PROJECT_DIR/../SDL2_mixer"
SRC_PATH="$PROJECT_DIR/src"
SNES_PATH="$PROJECT_DIR/snes"
GL_CORE_PATH="$PROJECT_DIR/third_party/gl_core"

# Check SDL paths
[ -d "$SDL_PATH" ] || echo "SDL path not found: $SDL_PATH"
[ -d "$SDL_MIXER_PATH" ] || echo "SDL Mixer path not found: $SDL_MIXER_PATH"

# Check source files
[ -d "$SRC_PATH" ] || echo "Source path not found: $SRC_PATH"
[ -d "$SNES_PATH" ] || echo "SNES path not found: $SNES_PATH"
[ -f "$GL_CORE_PATH/gl_core_3_1.c" ] || echo "GL Core file not found: $GL_CORE_PATH/gl_core_3_1.c"

# List all source files
echo "Source files:"
ls $SRC_PATH/*.c
ls $SNES_PATH/*.c
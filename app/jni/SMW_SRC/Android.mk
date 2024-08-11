LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := main

SDL_PATH := ../SDL2
SDL_MIXER_PATH := ../SDL2_mixer

LOCAL_C_INCLUDES := $(LOCAL_PATH)/$(SDL_PATH)/include \
                    $(LOCAL_PATH)/$(SDL_MIXER_PATH)/include 

# Add your application source files here...
LOCAL_SRC_FILES := $(wildcard $(LOCAL_PATH)/src/*.c) \
                   $(wildcard $(LOCAL_PATH)/src/snes/*.c) \
                   third_party/gl_core/gl_core_3_1.c

# Ensure SDL2 and SDL2_mixer libraries are correctly referenced
LOCAL_SHARED_LIBRARIES := libSDL2 libSDL2_mixer

LOCAL_LDLIBS := -lGLESv1_CM -lGLESv2 -lOpenSLES -llog -landroid

include $(BUILD_SHARED_LIBRARY)

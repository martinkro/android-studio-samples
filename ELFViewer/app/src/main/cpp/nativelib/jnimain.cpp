#include "utils/common.h"
#include <jni.h>

#define JNIREG_CLASS "info/loveai/jni/Native"

static const char* version = "elfviewer 3.3.30(2018/07/14)";

/*
 * boolean  Z   jboolean
 * byte     B   jbyte
 * char     C   jchar
 * short    S   jshort
 * int      I   jint
 * long     L   jlong
 * float    F   jfloat
 * double   D   jdouble
 * void     V
 * objects  Lfully-qualified-class-name;
 * arrays   [array-type
 * method   (argument-types)return-type
 * Object   jobject
 * Class    jclass
 * String   jstring
 * Object[] jobjectArray
 * boolean[]jbooleanArray
 * byte[]   jbyteArray
 * char[]   jcharArray
 * short[]  jshortArray
 * int[]    jintArray
 * long[]   jlongArray
 * float[]  jfloatArray
 * double[] jdoubleArray
 */

jstring StringFromJNI(JNIEnv *env,jobject clazz) {
    const char* hello = "Hello from C++";
    return env->NewStringUTF(hello);
}

static JNINativeMethod gNativeMethods[] = {
        {"stringFromJNI","()Ljava/lang/String;",(void*)StringFromJNI},

};
JNIEXPORT jint JNI_OnLoad(JavaVM* jvm, void* reserved)
{
    DEBUG_LOG("version:%s", version);
    JNIEnv* env = NULL;

    if (jvm->GetEnv((void**)&env,JNI_VERSION_1_4) == JNI_OK)
    {
        jclass clazz = env->FindClass(JNIREG_CLASS);
        if (clazz != NULL)
        {
            if (env->RegisterNatives(clazz,gNativeMethods, sizeof(gNativeMethods)/sizeof(gNativeMethods[0])) >= 0)
            {
                return JNI_VERSION_1_4;
            }
            else
            {
                __ASSERT(0);
            }
        }
        else
        {
            __ASSERT(0);
        }
    }
    else
    {
        __ASSERT(0);
    }
    return -1;
}

void __attribute__ ((constructor)) Constructor(void)
{
    // INIT_ARRAY
    DEBUG_LOG("constructor function");
}

#ifdef __cplusplus
extern "C" {
#endif

extern void _init()
{
    // _init function
    DEBUG_LOG("_init function");
}

#ifdef __cplusplus

};
#endif
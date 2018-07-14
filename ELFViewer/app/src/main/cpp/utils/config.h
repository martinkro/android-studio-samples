#ifndef UTILS_CONFIG_H
#define UTILS_CONFIG_H

#define __ENABLE_ASSERT__   // debug mode

#ifdef __ENABLE_ASSERT__
#define __ENABLE_LOG__
#endif

#define NS_LOVEAI_BEGIN namespace loveai{
#define NS_LOVEAI_END }
#define NS_LOVEAI_NAME loveai

#endif
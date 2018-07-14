#include "debug.h"
#include "log.h"

#include <cerrno>
#include <cstring>
#include <signal.h>

#include <sys/types.h>
#include <unistd.h>

NS_LOVEAI_BEGIN

void debug_assert(int express, const char* file, int line)
{
    if (express == 0)
    {
        ERROR_LOG("!ASSERT ERROR %s:%d", file,line);
        ERROR_LOG("!error:%d:%s", errno, strerror(errno));
        kill(getpid(), SIGKILL);
    }
}

NS_LOVEAI_END
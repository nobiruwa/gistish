#pragma once

#include "windows.h"

HRESULT IsTabletModeNow(HWND hwnd, int* mode);
HRESULT IsTabletModeNowDirectly(HWND hwnd, int* mode);
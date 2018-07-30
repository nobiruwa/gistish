using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace WpfAppTabletModeDetector
{
    [Guid("abe46ee4-aec0-4e9a-bcde-8fb7020a5c7a")]
    [InterfaceType(ComInterfaceType.InterfaceIsIUnknown)]
    public interface ITabletModeDetector
    {
        [PreserveSig]
        int IsTabletMode(IntPtr hwnd, ref int value);
    }
}

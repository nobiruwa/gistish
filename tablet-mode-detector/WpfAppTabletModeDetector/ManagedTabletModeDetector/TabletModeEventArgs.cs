using System;

namespace WpfAppTabletModeDetector
{
    public class TabletModeEventArgs
    {
        private int userInteractionMode;
        private Boolean isTabletMode;

        public int UserInteractionMode { get => userInteractionMode; set => userInteractionMode = value; }
        public bool IsTabletMode { get => isTabletMode; set => isTabletMode = value; }
    }
}
#include "stdafx.h"

#include "TabletModeDetectorComponent_h.h"
#include <uiviewsettingsinterop.h>
#include <windows.ui.viewmanagement.h>
#include <roapi.h>

#include <wrl.h>
#include <wrl/client.h>
#include <wrl/wrappers/corewrappers.h>
#include <wrl/implements.h>
#include <comutil.h>
#include "TabletModeDetectorComponent.h"


using namespace ABI::Windows::UI;
using namespace ABI::Windows::UI::ViewManagement;
using namespace ABI::Windows::Foundation;
using namespace Microsoft::WRL::Wrappers;
using namespace Microsoft::WRL;

class TabletModeDetectorComponent : public RuntimeClass<RuntimeClassFlags<ClassicCom>, ITabletModeDetectorComponent>
{
public:
	TabletModeDetectorComponent()
	{

	}

	~TabletModeDetectorComponent()
	{

	}

	STDMETHODIMP IsTabletMode(HWND hwndApp,  _Out_ int* value)
	{
		ComPtr<IUIViewSettingsInterop> uiViewSettingsInterop;
		HRESULT hr = GetActivationFactory(
			HStringReference(RuntimeClass_Windows_UI_ViewManagement_UIViewSettings).Get(),
			&uiViewSettingsInterop);
		if (SUCCEEDED(hr))
		{
			ComPtr<IUIViewSettings> uiViewSettings;
			hr = uiViewSettingsInterop->GetForWindow(hwndApp, IID_PPV_ARGS(&uiViewSettings));
			if (SUCCEEDED(hr))
			{
				UserInteractionMode mode;
				hr = uiViewSettings->get_UserInteractionMode(&mode);
				if (SUCCEEDED(hr))
				{
					
					switch (mode)
					{
					case UserInteractionMode_Mouse:
						*value = 0;
						break;

					case UserInteractionMode_Touch:
					default:
						*value = 1;
						break;
					}
				}
			}
		}
		return S_OK;
	}


};

CoCreatableClass(TabletModeDetectorComponent);

HRESULT IsTabletModeNow(HWND hwnd, int* mode)
{
	ComPtr<TabletModeDetectorComponent> detector = Make<TabletModeDetectorComponent>();


	return detector->IsTabletMode(hwnd, mode);
}

HRESULT IsTabletModeNowDirectly(HWND hwnd, int* value)
{
	ComPtr<IUIViewSettingsInterop> uiViewSettingsInterop;
	HRESULT hr = GetActivationFactory(
		HStringReference(RuntimeClass_Windows_UI_ViewManagement_UIViewSettings).Get(),
		&uiViewSettingsInterop);
	if (SUCCEEDED(hr))
	{
		ComPtr<IUIViewSettings> uiViewSettings;
		hr = uiViewSettingsInterop->GetForWindow(hwnd, IID_PPV_ARGS(&uiViewSettings));
		if (SUCCEEDED(hr))
		{
			UserInteractionMode mode;
			hr = uiViewSettings->get_UserInteractionMode(&mode);
			if (SUCCEEDED(hr))
			{

				switch (mode)
				{
				case UserInteractionMode_Mouse:
					*value = 0;
					break;

				case UserInteractionMode_Touch:
				default:
					*value = 1;
					break;
				}
			}
		}
	}
	return S_OK;
}
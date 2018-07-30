

/* this ALWAYS GENERATED file contains the definitions for the interfaces */


 /* File created by MIDL compiler version 8.01.0622 */
/* at Tue Jan 19 12:14:07 2038
 */
/* Compiler settings for TabletModeDetector.idl:
    Oicf, W1, Zp8, env=Win32 (32b run), target_arch=X86 8.01.0622 
    protocol : dce , ms_ext, c_ext, robust
    error checks: allocation ref bounds_check enum stub_data 
    VC __declspec() decoration level: 
         __declspec(uuid()), __declspec(selectany), __declspec(novtable)
         DECLSPEC_UUID(), MIDL_INTERFACE()
*/
/* @@MIDL_FILE_HEADING(  ) */



/* verify that the <rpcndr.h> version is high enough to compile this file*/
#ifndef __REQUIRED_RPCNDR_H_VERSION__
#define __REQUIRED_RPCNDR_H_VERSION__ 500
#endif

#include "rpc.h"
#include "rpcndr.h"

#ifndef __RPCNDR_H_VERSION__
#error this stub requires an updated version of <rpcndr.h>
#endif /* __RPCNDR_H_VERSION__ */

#ifndef COM_NO_WINDOWS_H
#include "windows.h"
#include "ole2.h"
#endif /*COM_NO_WINDOWS_H*/

#ifndef __TabletModeDetector_h_h__
#define __TabletModeDetector_h_h__

#if defined(_MSC_VER) && (_MSC_VER >= 1020)
#pragma once
#endif

/* Forward Declarations */ 

#ifndef __ITabletModeDetectorComponent_FWD_DEFINED__
#define __ITabletModeDetectorComponent_FWD_DEFINED__
typedef interface ITabletModeDetectorComponent ITabletModeDetectorComponent;

#endif 	/* __ITabletModeDetectorComponent_FWD_DEFINED__ */


#ifndef __TabletModeDetectorComponent_FWD_DEFINED__
#define __TabletModeDetectorComponent_FWD_DEFINED__

#ifdef __cplusplus
typedef class TabletModeDetectorComponent TabletModeDetectorComponent;
#else
typedef struct TabletModeDetectorComponent TabletModeDetectorComponent;
#endif /* __cplusplus */

#endif 	/* __TabletModeDetectorComponent_FWD_DEFINED__ */


/* header files for imported files */
#include "oaidl.h"
#include "ocidl.h"

#ifdef __cplusplus
extern "C"{
#endif 


#ifndef __ITabletModeDetectorComponent_INTERFACE_DEFINED__
#define __ITabletModeDetectorComponent_INTERFACE_DEFINED__

/* interface ITabletModeDetectorComponent */
/* [object][version][uuid] */ 


EXTERN_C const IID IID_ITabletModeDetectorComponent;

#if defined(__cplusplus) && !defined(CINTERFACE)
    
    MIDL_INTERFACE("abe46ee4-aec0-4e9a-bcde-8fb7020a5c7a")
    ITabletModeDetectorComponent : public IUnknown
    {
    public:
        virtual HRESULT STDMETHODCALLTYPE IsTabletMode( 
            /* [retval][out] */ int *value) = 0;
        
    };
    
    
#else 	/* C style interface */

    typedef struct ITabletModeDetectorComponentVtbl
    {
        BEGIN_INTERFACE
        
        HRESULT ( STDMETHODCALLTYPE *QueryInterface )( 
            ITabletModeDetectorComponent * This,
            /* [in] */ REFIID riid,
            /* [annotation][iid_is][out] */ 
            _COM_Outptr_  void **ppvObject);
        
        ULONG ( STDMETHODCALLTYPE *AddRef )( 
            ITabletModeDetectorComponent * This);
        
        ULONG ( STDMETHODCALLTYPE *Release )( 
            ITabletModeDetectorComponent * This);
        
        HRESULT ( STDMETHODCALLTYPE *IsTabletMode )( 
            ITabletModeDetectorComponent * This,
            /* [retval][out] */ int *value);
        
        END_INTERFACE
    } ITabletModeDetectorComponentVtbl;

    interface ITabletModeDetectorComponent
    {
        CONST_VTBL struct ITabletModeDetectorComponentVtbl *lpVtbl;
    };

    

#ifdef COBJMACROS


#define ITabletModeDetectorComponent_QueryInterface(This,riid,ppvObject)	\
    ( (This)->lpVtbl -> QueryInterface(This,riid,ppvObject) ) 

#define ITabletModeDetectorComponent_AddRef(This)	\
    ( (This)->lpVtbl -> AddRef(This) ) 

#define ITabletModeDetectorComponent_Release(This)	\
    ( (This)->lpVtbl -> Release(This) ) 


#define ITabletModeDetectorComponent_IsTabletMode(This,value)	\
    ( (This)->lpVtbl -> IsTabletMode(This,value) ) 

#endif /* COBJMACROS */


#endif 	/* C style interface */




#endif 	/* __ITabletModeDetectorComponent_INTERFACE_DEFINED__ */



#ifndef __TabletModeDetectorLib_LIBRARY_DEFINED__
#define __TabletModeDetectorLib_LIBRARY_DEFINED__

/* library TabletModeDetectorLib */
/* [version][uuid] */ 


EXTERN_C const IID LIBID_TabletModeDetectorLib;

EXTERN_C const CLSID CLSID_TabletModeDetectorComponent;

#ifdef __cplusplus

class DECLSPEC_UUID("102aee51-6848-4611-b039-98581d4f1d06")
TabletModeDetectorComponent;
#endif
#endif /* __TabletModeDetectorLib_LIBRARY_DEFINED__ */

/* Additional Prototypes for ALL interfaces */

/* end of Additional Prototypes */

#ifdef __cplusplus
}
#endif

#endif



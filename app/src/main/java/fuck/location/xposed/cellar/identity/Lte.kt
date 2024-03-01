package fuck.location.xposed.cellar.identity

import android.os.Build
import android.telephony.CellIdentityLte
import android.telephony.ClosedSubscriberGroupInfo
import de.robv.android.xposed.XposedBridge
import fuck.location.xposed.helpers.ConfigGateway
import org.lsposed.hiddenapibypass.HiddenApiBypass

class Lte {
    @ExperimentalStdlibApi
    fun alterCellIdentity(cellIdentityLte: CellIdentityLte): CellIdentityLte {
        val constructor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            HiddenApiBypass.getDeclaredConstructor(
                CellIdentityLte::class.java,
                Int::class.java,    // ci
                Int::class.java,    // pci
                Int::class.java,    // tac
                Int::class.java,    // earfcn
                IntArray::class.java,  // bands
                Int::class.java,    // bandwidth
                String::class.java, // mccStr
                String::class.java, // mncStr
                String::class.java, // alphal
                String::class.java, // alphas
                Collection::class.java, // additionalPlmns
                ClosedSubscriberGroupInfo::class.java,  // csgInfo
            )
        } else {
            HiddenApiBypass.getDeclaredConstructor(
                CellIdentityLte::class.java,
                Int::class.java,    // ci
                Int::class.java,    // pci
                Int::class.java,    // tac
                Int::class.java,    // earfcn
                Int::class.java,    // bandwidth
                String::class.java, // mccStr
                String::class.java, // mncStr
                String::class.java, // alphal
                String::class.java, // alphas
            )
        }

        val bands = intArrayOf(3)
        val plmns = emptySet<String>()

        val customResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            constructor.newInstance(
                150311218,//ConfigGateway.get().readFakeLocation()!!.eci,  // ECI
                130,//ConfigGateway.get().readFakeLocation()!!.pci,  // Pci
                62479,//ConfigGateway.get().readFakeLocation()!!.tac,  // Tac
                1800,//ConfigGateway.get().readFakeLocation()!!.earfcn,  // Earfcn
                bands,//cellIdentityLte.bands,
                20,//ConfigGateway.get().readFakeLocation()!!.bandwidth,  // bandwidth
                "460",//cellIdentityLte.mccString,
                "01",//cellIdentityLte.mncString,
                "CHN-UNICOM",//cellIdentityLte.operatorAlphaLong,
                "UNICOM",//cellIdentityLte.operatorAlphaShort,
                cellIdentityLte.additionalPlmns,
                cellIdentityLte.closedSubscriberGroupInfo
            )
        } else {
            constructor.newInstance(
                ConfigGateway.get().readFakeLocation()!!.eci,  // CI
                ConfigGateway.get().readFakeLocation()!!.pci,  // Pci
                ConfigGateway.get().readFakeLocation()!!.tac,  // Tac
                ConfigGateway.get().readFakeLocation()!!.earfcn,  // Earfcn
                ConfigGateway.get().readFakeLocation()!!.bandwidth,  // bandwidth
                cellIdentityLte.mccString,
                cellIdentityLte.mncString,
                cellIdentityLte.operatorAlphaLong,
                cellIdentityLte.operatorAlphaShort,
            )
        } as CellIdentityLte

        XposedBridge.log("FL: [Cellar] additionalPlmns: $cellIdentityLte.additionalPlmns")
        XposedBridge.log("FL: [Cellar] Returning custom result: $customResult")

        return customResult
    }
}
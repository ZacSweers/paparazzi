package app.cash.paparazzi

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.compose.runtime.Composable
import com.android.ide.common.rendering.api.SessionParams.RenderingMode

interface Paparazzi {
  val layoutInflater: LayoutInflater
  val resources: Resources
  val context: Context

  fun init(testName: TestName)
  fun dispose()
  fun <V : View> inflate(@LayoutRes layoutId: Int): V = layoutInflater.inflate(layoutId, null) as V
  fun snapshot(name: String? = null, composable: @Composable () -> Unit)
  fun snapshot(view: View, name: String? = null)
  fun gif(
    view: View,
    name: String? = null,
    start: Long = 0L,
    end: Long = 500L,
    fps: Int = 30
  )
  fun unsafeUpdateConfig(
    deviceConfig: DeviceConfig? = null,
    theme: String? = null,
    renderingMode: RenderingMode? = null
  )
}

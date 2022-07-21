package app.cash.paparazzi.rule

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Environment
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.PaparazziCore
import app.cash.paparazzi.PaparazziCore.Companion.determineHandler
import app.cash.paparazzi.RenderExtension
import app.cash.paparazzi.SnapshotHandler
import app.cash.paparazzi.TestName
import app.cash.paparazzi.detectEnvironment
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class PaparazziRule private constructor(private val paparazzi: PaparazziCore) : TestRule,
  Paparazzi by paparazzi {

  companion object {
    fun paparazziRule(
      environment: Environment = detectEnvironment(),
      deviceConfig: DeviceConfig = DeviceConfig.NEXUS_5,
      theme: String = "android:Theme.Material.NoActionBar.Fullscreen",
      renderingMode: RenderingMode = RenderingMode.NORMAL,
      appCompatEnabled: Boolean = true,
      maxPercentDifference: Double = 0.1,
      snapshotHandler: SnapshotHandler = determineHandler(maxPercentDifference),
      renderExtensions: Set<RenderExtension> = setOf()
    ): PaparazziRule =
      PaparazziRule(
        PaparazziCore(
          environment,
          deviceConfig,
          theme,
          renderingMode,
          appCompatEnabled,
          maxPercentDifference,
          snapshotHandler,
          renderExtensions
        )
      )
  }

  override fun apply(base: Statement, description: Description): Statement = object : Statement() {
    override fun evaluate() {
      paparazzi.init(description.toTestName())
      try {
        base.evaluate()
      } finally {
        paparazzi.dispose()
      }
    }
  }

  private fun Description.toTestName(): TestName {
    val fullQualifiedName = className
    val packageName = fullQualifiedName.substringBeforeLast('.', missingDelimiterValue = "")
    val className = fullQualifiedName.substringAfterLast('.')
    return TestName(packageName, className, methodName)
  }
}

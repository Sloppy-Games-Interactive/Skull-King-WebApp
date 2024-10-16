package de.htwg.se.skullking.view.gui

import de.htwg.se.skullking.controller.ControllerComponent.{ControllerEvents, IController}
import de.htwg.se.skullking.model.StateComponent.Phase
import de.htwg.se.skullking.util.{ObservableEvent, Observer}
import de.htwg.se.skullking.view.gui.scenes.{GameScene, PreGameScene, SettingsScene, TitleScene}
import scalafx.Includes.*
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.image.Image
import scalafx.scene.text.Font
import scalafx.scene.{ImageCursor, Scene}

import scala.compiletime.uninitialized

object Styles {
  val mainCss: String = getClass.getResource("/models/view/gui/resources/styles/main.css").toExternalForm
  val gameSceneCss: String = getClass.getResource("/models/view/gui/resources/styles/scenes/gameScene.css").toExternalForm
  val titleSceneCss: String = getClass.getResource("/models/view/gui/resources/styles/scenes/titleScene.css").toExternalForm
  val preGameSceneCss: String = getClass.getResource("/models/view/gui/resources/styles/scenes/preGameScene.css").toExternalForm
  val settingsSceneCss: String = getClass.getResource("/models/view/gui/resources/styles/scenes/settingsScene.css").toExternalForm
  
  // components
  val gameButtonCss: String = getClass.getResource("/models/view/gui/resources/styles/components/gameButton.css").toExternalForm
  val playerListRowCss: String = getClass.getResource("/models/view/gui/resources/styles/components/playerListRow.css").toExternalForm
  val panelCss: String = getClass.getResource("/models/view/gui/resources/styles/components/panel.css").toExternalForm
  val PauseMenuPanelCss: String = getClass.getResource("/models/view/gui/resources/styles/components/pauseMenu.css").toExternalForm
}

class Gui(controller: IController) extends JFXApp3 with Observer {
  controller.add(this)

  private var titleScene: TitleScene = uninitialized
  private var preGameScene: PreGameScene = uninitialized
  private var settingsScene: SettingsScene = uninitialized
  private var gameScene: GameScene = uninitialized

  private val windowWidth = 1440
  private val windowHeight = 960

  private val minWindowWidth = 500
  private val minWindowHeight = 300

  Font.loadFont(getClass.getResourceAsStream("/models/view/gui/resources/fonts/Pieces_of_Eight.ttf"), 50)

  override def start(): Unit = {
    val customCursorImage = new Image("/images/cursor.png")
    val customCursor = new ImageCursor(customCursorImage)

    titleScene = TitleScene(
      controller = controller,
      windowHeight = windowHeight,
      windowWidth = windowWidth,
      onClickPlayButton = () => showPreGameScene(),
      onClickSettingsButton = () => stage.setScene(settingsScene),
      onClickQuitButton = () => controller.quit
    )

    settingsScene = SettingsScene(
      controller = controller,
      windowWidth = windowWidth,
      windowHeight = windowHeight,
      onClickBackButton = () => stage.setScene(titleScene)
    )

    gameScene = GameScene(
      controller = controller,
      windowWidth = windowWidth,
      windowHeight = windowHeight,
      onClickQuitBtn = () => stage.setScene(titleScene)
    )

    stage = new JFXApp3.PrimaryStage {
      resizable = false
      title = "Skull King - Card Game"
      scene = titleScene
      minWidth = minWindowWidth
      minHeight = minWindowHeight
      icons. += (new Image(getClass.getResourceAsStream("/models/view/gui/resources/images/icon.png")))

      // override close button function
      onCloseRequest = () => {
        controller.quit
      }
    }
  }

  override def update(event: ObservableEvent): Unit = {
    event match {
      case ControllerEvents.Quit => Platform.exit()
      case ControllerEvents.NewGame => showPreGameScene()
      case ControllerEvents.PlayerAdded =>{
        if Phase.PrepareTricks == controller.state.phase then
          showGameScene()
      }
      case ControllerEvents.LoadGame =>
        showGameScene()
      case _ => {
        println("Update")
      }
    }
  }

  private def showGameScene(): Unit = {
    Platform.runLater(() => stage.setScene(gameScene))
  }

  private def showPreGameScene(): Unit = {
    preGameScene = PreGameScene(
      controller = controller,
      windowWidth = windowWidth,
      windowHeight = windowHeight,
      onClickStartGameButton = () => stage.setScene(gameScene),
    )

    Platform.runLater(() => stage.setScene(preGameScene))
  }
}


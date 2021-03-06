package lt.maze.dialog

import net.gtaun.shoebill.constant.DialogStyle
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.event.dialog.DialogResponseEvent
import net.gtaun.util.event.EventManager

/**
 * @author Bebras
 * 2016.08.10.
 */
open class MsgBoxDialog(player: Player, eventManager: EventManager) : AbstractDialog(player, eventManager) {

    override val style = DialogStyle.MSGBOX
    private var clickOkHandler: ((MsgBoxDialog) -> Unit)? = null

    override fun onClickOk(event: DialogResponseEvent) {
        clickOkHandler?.invoke(this)
    }


    override fun onClickCancel() {
        clickCancelHandler?.invoke(this)
    }

    companion object {
        fun create(p: Player, eventManager: EventManager, block: MsgBoxDialogBuilder.() -> Unit): MsgBoxDialog {
            val b = MsgBoxDialogBuilder(MsgBoxDialog(p, eventManager))
            b.block()
            return b.build()
        }
    }

    open class AbstractMsgBoxDialogBuilder<T, V>(open val dialog: T): AbstractDialog.AbstractDialogBuilder<T, V>(dialog)
        where T: MsgBoxDialog, V: AbstractMsgBoxDialogBuilder<T, V> {

        @Deprecated("Inconsistent", ReplaceWith("onClickOk(handler)"))
        fun clickOk(handler: (MsgBoxDialog) -> Unit) = onClickOk(handler)

        @Suppress("UNCHECKED_CAST")
        fun onClickOk(handler: (MsgBoxDialog) -> Unit): V {
            dialog.clickOkHandler = handler
            return this as V
        }

    }

    open class MsgBoxDialogBuilder(dialog: MsgBoxDialog): AbstractMsgBoxDialogBuilder<MsgBoxDialog, MsgBoxDialogBuilder>(dialog) {

    }

}
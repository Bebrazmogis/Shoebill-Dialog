package lt.maze.dialog

import net.gtaun.shoebill.constant.DialogStyle
import net.gtaun.shoebill.entities.Player
import net.gtaun.shoebill.event.dialog.DialogResponseEvent
import net.gtaun.util.event.EventManager

/**
 * @author Bebras
 * 2016.08.10.
 */
open class ListDialog(player: Player, eventManager: EventManager): AbstractDialog(player, eventManager) {

    override val style = DialogStyle.LIST
    private var clickOkHandler: ((ListDialog, ListDialogItem) -> Unit)? = null
    protected val items: MutableList<ListDialogItem> = mutableListOf()

    override fun onClickOk(event: DialogResponseEvent) {
        val item = items[event.listItem]
        item.onSelect()
        onSelectItem(item)
    }

    open fun onSelectItem(item: ListDialogItem) {
        clickOkHandler?.invoke(this, item)
    }

    override fun prepareBodyString(): String {
        var body = ""
        items.filter { it.enabled.invoke() }.forEach {
            body += it.itemText + "\n"
        }
        return body
    }

    override fun destroy() {
        items.clear()
        super.destroy()
    }

    companion object {
        fun create(player: Player, eventManager: EventManager, block: (ListDialogBuilder.() -> Unit)): ListDialog {
            val builder = ListDialogBuilder(ListDialog(player, eventManager))
            builder.block()
            return builder.build()
        }
    }

    abstract class AbstractListDialogBuilder<T: ListDialog, V: AbstractListDialogBuilder<T, V>>(val dialog: T): AbstractDialogBuilder<T, V>(dialog) {

        @Suppress("UNCHECKED_CAST")
        fun onSelectItem(apply: (ListDialog, ListDialogItem) -> Unit): V {
            dialog.clickOkHandler = apply
            return this as V
        }

        @Suppress("UNCHECKED_CAST")
        fun items(items: List<ListDialogItem>): V {
            items.forEach { it.dialog = dialog }
            dialog.items.addAll(items)
            return this as V
        }

        @Suppress("UNCHECKED_CAST")
        fun item(item: ListDialogItem): V {
            item.dialog = dialog
            dialog.items.add(item)
            return this as V
        }

        fun item(itemText: String): V {
            return item {
                itemText { itemText }
            }
        }

        fun item(itemText: String, data: Any): V {
            return item {
                itemText { itemText }
                data { data }
            }
        }

        fun item(itemText: String, handler: (ListDialogItem) -> Unit): V {
            return item {
                itemText { itemText }
                selectHandler(handler)
            }
        }

        fun item(itemText: String, data: Any, handler: (ListDialogItem) -> Unit) = item({
            itemText { itemText }
            data { data }
            selectHandler(handler)
        })

        fun item(itemProvider: ListDialogItem.() -> Unit): V {
            val item = ListDialogItem()
            item.itemProvider()
            return item(item)
        }
    }
    class ListDialogBuilder(dialog: ListDialog): AbstractListDialogBuilder<ListDialog, ListDialogBuilder>(dialog) {

    }
}
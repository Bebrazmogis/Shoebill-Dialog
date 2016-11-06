package lt.maze.dialog

/**
 * @author Bebras
 * 2016.08.11.
 */
open class ListDialogItem(open var itemText: String, open var data: Any?, protected open var selectHandler: ((ListDialogItem) -> Unit)?, open var enabled: () -> Boolean) {

    internal var currentDialog: ListDialog? = null

    constructor(): this("", null, null, { true })
    constructor(itemText: String): this(itemText, null, null, { true })
    constructor(itemText: String, selectHandler: ((ListDialogItem) -> Unit)?): this(itemText, null, selectHandler, { true })

    fun itemText(func: () -> String) {
        itemText = func.invoke()
    }

    fun data(func: () -> Any) {
        data = func.invoke()
    }

    fun selectHandler(func: (ListDialogItem) -> Unit) {
        selectHandler = func
    }

    fun enabled(func: () -> Boolean) {
        enabled = func
    }

    fun getDialog(): ListDialog {
        return currentDialog!!
    }

    open fun onSelect() {
        selectHandler?.invoke(this)
    }

    companion object {
        fun create(init: (ListDialogItem.() -> Unit)): ListDialogItem {
            val item = ListDialogItem()
            item.init()
            return item
        }
    }

}
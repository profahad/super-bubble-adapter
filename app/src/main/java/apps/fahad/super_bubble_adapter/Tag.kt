package apps.fahad.super_bubble_adapter

import apps.fahad.libs.super_bubble_adapter.interfaces.OptionIcon
import apps.fahad.libs.super_bubble_adapter.interfaces.OptionTitle

data class Tag(
    val title: String
) : OptionTitle, OptionIcon {

    override val optionTitle: String
        get() = title

    override val optionIcon: String
        get() = "https://static.vecteezy.com/system/resources/previews/001/198/092/original/world-png.png"
}
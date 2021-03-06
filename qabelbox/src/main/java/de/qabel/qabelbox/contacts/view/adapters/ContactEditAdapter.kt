package de.qabel.qabelbox.contacts.view.adapters

import android.view.View
import android.widget.LinearLayout
import de.qabel.core.config.Identities
import de.qabel.core.config.Identity
import de.qabel.core.extensions.contains
import de.qabel.core.index.formatPhoneNumberReadable
import de.qabel.core.index.isValidPhoneNumber
import de.qabel.core.ui.displayName
import de.qabel.core.ui.initials
import de.qabel.qabelbox.R
import de.qabel.qabelbox.contacts.dto.ContactDto
import de.qabel.qabelbox.contacts.extensions.color
import de.qabel.qabelbox.contacts.extensions.contactColors
import de.qabel.qabelbox.contacts.view.widgets.ContactIconDrawable
import de.qabel.qabelbox.contacts.view.widgets.IdentityIconDrawable
import de.qabel.qabelbox.ui.extensions.setOrGone
import kotlinx.android.synthetic.main.contact_edit_identity_switch.view.*
import kotlinx.android.synthetic.main.fragment_contact_edit.view.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.layoutInflater


class ContactEditAdapter() {

    var view: View? = null

    fun loadContact(contact: ContactDto, identities: Identities) {
        view?.apply {
            contact_icon_border.background = ContactIconDrawable(contact.contactColors(context))
            val size = dip(95)
            contact_initials.background = IdentityIconDrawable(text = contact.contact.initials(),
                    width = size, height = size,
                    color = contact.contact.color(context))

            editTextContactNick.setText(contact.contact.displayName())
            editTextContactName.text = contact.contact.alias
            contact_ignored_switch.isChecked = contact.contact.isIgnored
            val mail = contact.contact.email ?: ""
            val phone = if (isValidPhoneNumber(contact.contact.phone))
                formatPhoneNumberReadable(contact.contact.phone) else ""

            text_phone.setOrGone(phone)
            text_email.setOrGone(mail)

            contact_identities.removeAllViews()
            identities.entities.forEach {
                addIdentityToggle(it, contact.identities.contains(it.keyIdentifier))
            }
        }
    }

    private fun addIdentityToggle(identity: Identity, active: Boolean) {
        view?.apply {
            (context.layoutInflater.inflate(R.layout.contact_edit_identity_switch, null) as LinearLayout).let {
                (it.identity_switch_control).let {
                    it.text = identity.alias
                    it.isChecked = active
                }
                it.setTag(R.id.identity_switch_control, identity.id)
                it.identity_icon.background = IdentityIconDrawable(
                        text = identity.initials(),
                        color = identity.color(context))
                it.identity_initial.text = identity.initials()
                contact_identities.addView(it)
            }
        }
    }

    fun getNickname(): String = view?.editTextContactNick?.text.toString()
    fun getIdentityIds(): List<Int> {
        val ids = mutableListOf<Int>()
        view?.contact_identities?.let {
            for (i in 0 until it.childCount) {
                it.getChildAt(i).let {
                    if (it.identity_switch_control.isChecked) {
                        ids.add(it.getTag(R.id.identity_switch_control) as Int)
                    }
                }
            }
        }
        return ids
    }

    fun isContactIgnored() : Boolean = view?.contact_ignored_switch?.isChecked ?: false

}

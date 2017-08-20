package de.devfest.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object ExternalAppUtils {

    fun openLink(context: Context, link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        context.startActivity(intent)
    }
}

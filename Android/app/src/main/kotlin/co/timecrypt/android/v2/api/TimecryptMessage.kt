package co.timecrypt.android.v2.api

import com.squareup.moshi.Json
import java.util.*

/**
 * Interpretation of a Timecrypt message. Available values are:
 *
 * @param text Contents of the message, *must not be empty*
 * @param views How many times is this message allowed to be shown, can be empty, defaults to 1
 * @param destructDate When to self-destruct the message (format: yyyy-mm-dd), must be at least one day in the future, can be empty, defaults to `tomorrow`
 * @param emailTo Where to send the invitation to this message, can be empty, no default
 * @param emailFrom Where to send the "message read" notification, can be empty, no default
 * @param title Title of the message, can be empty, no default
 * @param password Which passphrase to use to encrypt the message, can be empty, defaults to datastore's passphrase
 */
class TimecryptMessage(
        val text: String,
        val views: Int = 1,
        @Json(name = "lifetime") val destructDate: Date?,
        @Json(name = "email_to") val emailTo: String?,
        @Json(name = "email_from") val emailFrom: String?,
        val title: String?,
        val password: String?
) {

    constructor(text: String) : this(text, 1, null, null, null, null, null)

    constructor(text: String, views: Int) : this(text, views, null, null, null, null, null)

}
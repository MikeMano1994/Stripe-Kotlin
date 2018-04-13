@file:Suppress("DEPRECATION")

package cog.com.stripe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.braintreepayments.cardform.view.CardForm
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token

class MainActivity : AppCompatActivity() {

    private var cardForm: CardForm? = null
    private var checkout_button: Button? = null
    private var values: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardForm = findViewById(R.id.bt_card_form)
        checkout_button = findViewById(R.id.checkout_card)
        values = findViewById(R.id.values)
        performAction()
    }

    private fun performAction()
    {
        cardForm?.cardRequired(true)
                ?.expirationRequired(true)
                ?.cvvRequired(true)
                ?.actionLabel("CheckOut")
                ?.setup(this)

        checkout_button?.setOnClickListener(View.OnClickListener {
            if (cardForm!!.isValid) {
                getStripeToken(cardForm!!.cardNumber, cardForm!!.expirationMonth, cardForm!!.expirationYear, cardForm!!.cvv)
            } else {
                cardForm!!.validate()
            }
        })

        cardForm?.setOnClickListener(View.OnClickListener {
            if (cardForm!!.isValid) {
                getStripeToken(cardForm!!.cardNumber, cardForm!!.expirationMonth, cardForm!!.expirationYear, cardForm!!.cvv)
            } else{
                cardForm!!.validate()
            }
        })
        values?.setOnClickListener(View.OnClickListener {
            values?.setText("")
        })
    }

    private fun getStripeToken(cardNumber: String, expirationMonth: String, expirationYear: String, cvv: String)
    {

        var card: Card? = null
        var stripe: Stripe? = null

        card = Card(cardNumber, Integer.parseInt(expirationMonth), Integer.parseInt(expirationYear), cvv)

        stripe = Stripe(this,"pk_test_XodpcVh41CGznIKrE4cEEmDs")// your publishable key
        stripe.createToken(
                card, object : TokenCallback{
            override fun onSuccess(token: Token){

               /* var a = token.bankAccount
                var b = token.card
                var c = token.created
                var d = token.livemode
                var e = token.type
                var f = token.used
                var g = token.id

                values?.setText("Bank = $a \nCard = $b \nCreated = $c \nLivemode = $d \nType = $e \nUsed = $f \nId = $g")*/
                Toast.makeText(this@MainActivity,"payment ${token.id}",Toast.LENGTH_SHORT).show()
            }
            override fun onError(exception: Exception){
                Toast.makeText(this@MainActivity,"error ${exception.localizedMessage}",Toast.LENGTH_SHORT).show()
            }
        }
        )
    }
}

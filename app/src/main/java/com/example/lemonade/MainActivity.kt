package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    /**
     * NÃO ALTERE NENHUMA VARIÁVEL OU NOME DE VALOR OU SEUS VALORES INICIAIS.
     *
     * Espera-se que qualquer coisa que chame-se var em vez de val seja alterada nas funções, PORÉM NÃO
     * alterar seus valores iniciais declarados aqui, isso pode fazer com que o aplicativo não funcione corretamente.
     */
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT representa o estado "pegue um limão"
    private val SELECT = "select"
    // SQUEEZE representa o estado "espremer limão"
    private val SQUEEZE = "squeeze"
    // DRINK representa o estado "beber limonada"
    private val DRINK = "drink"
    // RESTART representa o estado onde a limonada foi bebida e o copo está vazio
    private val RESTART = "restart"
    // Padrão do estado para selecionar
    private var lemonadeState = "select"
    // Padrão lemonSize para -1
    private var lemonSize = -1
    // Padrão do squeezeCount para -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // === NÃO ALTERE O CÓDIGO NO SEGUINTE IF ===
        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        // === END IF ===

        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()
        lemonImage!!.setOnClickListener {
            clickLemonImage()
        }
        lemonImage!!.setOnLongClickListener {
            showSnackbar()
            false
        }
    }

    /**
     * === NÃO ALTERE ESTE MÉTODO ===
     *
     * Este método salva o estado do aplicativo se for colocado em segundo plano.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * Clicar irá provocar uma resposta diferente dependendo do estado.
     * Este método determina o estado e prossegue com a ação correta.
     */
    private fun clickLemonImage() {
        // altera os dados com base no lemonadeState atual.
        when (lemonadeState) {
            SELECT -> {
                //Se o estado é SELECT isso significa que ao tocar, tentaremos espremer o limão, então defina o estado como SQUEEZE
                lemonadeState = SQUEEZE
                //determina o tamanho da árvore pela função peek()
                val tree: LemonTree = lemonTree
                lemonSize = tree.pick();
                //no começa o limão ainda não foi espremido, então a contagem inicia em zero
                squeezeCount = 1
            }
            SQUEEZE -> {
                //agora que estamos espremendo, a contagem aumentará em 1 e o tamanho diminuirá em 1
                squeezeCount += 1
                lemonSize -= 1
                //agora o lemonadeState ficara no estado Squeeze enquanto o limão não terminar de ser espremido(lemonSize=0)
                //sae o lemonSize for 0, então o estado é alterado para DRINK
                lemonadeState = if (lemonSize == 0) {
                    DRINK
                } else SQUEEZE
            }
            //se o estado for Drink então iremos beber a limonada e o estado será restart
            DRINK -> {
                lemonadeState = RESTART
                lemonSize = -1
            }
            //restart agora significa que teremos que escolher o limão da arvore.
            RESTART -> lemonadeState = SELECT
        }
        //como resultado do código acima nós iremos chamar a função setViewElements() para definir a view de acordo.
        setViewElements()
    }

    /**
     * Define os elementos da view de acordo com o estado.
     */
    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)
        val lemonImage: ImageView = findViewById(R.id.image_lemon_state)

        //Quealquer que seja o estado, nós precisamos definir as imagens e textos dos mesmos.
        when (lemonadeState) {
            SELECT -> {
                textAction.text = getString(R.string.lemon_select)
                lemonImage.setImageResource(R.drawable.lemon_tree)
            }
            SQUEEZE -> {
                textAction.text = getString(R.string.lemon_squeeze)
                lemonImage.setImageResource(R.drawable.lemon_squeeze)
            }
            DRINK -> {
                textAction.text = getString(R.string.lemon_drink)
                lemonImage.setImageResource(R.drawable.lemon_drink)
            }
            RESTART -> {
                textAction.text = getString(R.string.lemon_empty_glass)
                lemonImage.setImageResource(R.drawable.lemon_restart)
            }
        }
    }

    /**
     * === NÃO ALTERE ESTE MÉTODO ===
     *
     * Um clique longo irá mostrar quantas vezes o limão foi espremido.
     */
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

/**
 * Um arvore de limões com um método "pick" um limão. O "size" do limão é randomico
 * e determina quantas vezes o limão precisa ser espremido antes de se ter uma limonada.
 */
class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}

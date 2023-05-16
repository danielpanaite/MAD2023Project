import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.courtreservationapplicationjetpack.R
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.option.OptionDialog
import com.maxkeppeler.sheets.option.models.DisplayMode
import com.maxkeppeler.sheets.option.models.Option
import com.maxkeppeler.sheets.option.models.OptionConfig
import com.maxkeppeler.sheets.option.models.OptionDetails
import com.maxkeppeler.sheets.option.models.OptionSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OptionSample3(closeSelection: () -> Unit) {

    val options = listOf(
        Option(
            IconSource(R.drawable.ic_calcio5),
            titleText = "Calcio a 5"
        ),
        Option(
            IconSource(R.drawable.ic_basket),
            titleText = "Basket",
        ),
        Option(
            IconSource(R.drawable.ic_beachvolley),
            titleText = "Beach Volley",
            selected = true
        ),
        Option(
            IconSource(R.drawable.ic_tennis),
            titleText = "Tennis",
            details = OptionDetails(
                "Il Tennis",
                "È uno sport brutto per persone a cui non piace il calcio"
            )
        ),
//        Option(
//            IconSource(R.drawable.ic_bottom_profile),
//            titleText = "Cherries",
//        ),
//        Option(
//            IconSource(R.drawable.ic_bottom_profile),
//            titleText = "Citrus",
//        ),
    )

    OptionDialog(
        state = rememberUseCaseState(visible = true, onCloseRequest = { closeSelection() }),
        selection = OptionSelection.Multiple(
            minChoices = 2,
            maxChoices = 3,
            options = options
        ) { indicies, options ->
            // Handle selections
        },
        config = OptionConfig(
            mode = DisplayMode.GRID_VERTICAL,
        )
    )
}
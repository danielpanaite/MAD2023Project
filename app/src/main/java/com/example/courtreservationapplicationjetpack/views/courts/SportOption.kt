import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.courtreservationapplicationjetpack.R
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.option.OptionDialog
import com.maxkeppeler.sheets.option.models.DisplayMode
import com.maxkeppeler.sheets.option.models.Option
import com.maxkeppeler.sheets.option.models.OptionConfig
import com.maxkeppeler.sheets.option.models.OptionSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OptionSample3(
    sportList: List<String>,
    optionState: UseCaseState,
    pickedSport: String,
    setPickedSport: (String) -> Unit, closeSelection: () -> Unit) {

    val options = sportList.filter { sport ->
        when (sport) {
            "calcio" -> true
            "basket" -> true
            "beach volley" -> true
            "tennis" -> true
            "pallamano" -> true
            "pallavolo"-> true
            "rugby" -> true
            "softball" -> true
            else -> true
        }
    }.map { sport ->
        Option(
            IconSource(getIconResourceForSport(sport)),
            titleText = sport,
            selected = sport == pickedSport
        )
    }

    OptionDialog(
        state = optionState,
        selection = OptionSelection.Single(
            options = options
        ) { indices, _ ->
            setPickedSport(sportList[indices])
        },
        config = OptionConfig(
            mode = DisplayMode.GRID_VERTICAL,
        ),
    )
}

@Composable
private fun getIconResourceForSport(sport: String): Int {
    return when (sport) {
        "calcio" -> R.drawable.ic_calcio5
        "basket" -> R.drawable.ic_basket
        "beach volley" -> R.drawable.ic_beachvolley
        "pallavolo" -> R.drawable.ic_volley
        "tennis" -> R.drawable.ic_tennis
        "pallamano" -> R.drawable.pallamano
        "rugby" -> R.drawable.ic_rugby
        "softball" -> R.drawable.ic_softball
        else -> R.drawable.ic_bottom_profile // Icona di default per gli sport non riconosciuti
    }
}

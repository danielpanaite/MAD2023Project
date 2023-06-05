
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.firestore.CourtViewModel
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.option.OptionDialog
import com.maxkeppeler.sheets.option.models.DisplayMode
import com.maxkeppeler.sheets.option.models.Option
import com.maxkeppeler.sheets.option.models.OptionConfig
import com.maxkeppeler.sheets.option.models.OptionSelection
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OptionSample3(
    sportList: List<String>,
    optionState: UseCaseState,
    pickedSport: MutableState<String>,
    firebaseCourtViewModel: CourtViewModel,
    pickedCity: MutableState<String>,
    ) {

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
            titleText = sport.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }.substringBefore(' '),
            selected = sport == pickedSport.value,
        )
    }

    OptionDialog(
        state = optionState,
        selection = OptionSelection.Single(
            options = options
        ) { indices, _ ->
            pickedSport.value = sportList[indices]
            firebaseCourtViewModel.getCourtsBySport(sportList[indices], pickedCity.value)
        },
        config = OptionConfig(
            mode = DisplayMode.GRID_VERTICAL,
        ),
    )
}

@Composable
fun getIconResourceForSport(sport: String): Int {
    return when (sport) {
        "calcio" -> R.drawable.ic_calcio5
        "basket" -> R.drawable.ic_basket
        "beach volley" -> R.drawable.ic_beachvolley
        "pallavolo" -> R.drawable.ic_volley
        "tennis" -> R.drawable.ic_tennis
        "pallamano" -> R.drawable.pallamano
        "rugby" -> R.drawable.ic_rugby
        "softball" -> R.drawable.ic_softball
        else ->  R.drawable.ic_question_mark // Icona di default per gli sport non riconosciuti
    }
}

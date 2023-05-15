package com.example.courtreservationapplicationjetpack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.models.reservations.Reservation
import com.example.courtreservationapplicationjetpack.ui.theme.GreyItemInactive
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.flow.filterNotNull
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


private val pageBackgroundColor: Color @Composable get() = MaterialTheme.colorScheme.background
private val itemBackgroundColor: Color @Composable get() = MaterialTheme.colorScheme.primaryContainer
private val toolbarColor: Color @Composable get() = MaterialTheme.colorScheme.primary
private val selectedItemColor: Color @Composable get() = MaterialTheme.colorScheme.onPrimaryContainer
private val inActiveTextColor: Color @Composable get() = GreyItemInactive

@Composable
fun MonthCalendar(
    reservations: List<Reservation>,
    //navigateToDetailsReservation: () -> Unit,
    //onReservationClick: (Reservation) -> Unit,
) {
    val reservationFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val reservationList = reservations.groupBy { LocalDate.parse(it.date, reservationFormatter) }
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }
    val colors: List<Color> = listOf(Color.Red, Color.Blue, Color.Magenta, Color.Cyan, Color.Green, Color.Yellow)
    val reservationsInSelectedDate = remember {
        derivedStateOf {
            val date = selection?.date
            if (date == null) emptyList() else reservationList[date].orEmpty()
        }
    }
    //main column that contains the whole page
    Column( modifier = Modifier.fillMaxSize().background(pageBackgroundColor)) {
        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
            outDateStyle = OutDateStyle.EndOfGrid,
        )
        val visibleMonth = rememberFirstCompletelyVisibleMonth(state)
        LaunchedEffect(visibleMonth) {
            // Clear selection if we scroll to a new month.
            selection = null
        }

        // Draw dark content on light background.
        CompositionLocalProvider(LocalContentColor provides lightColorScheme().onSurface) {
            Surface(shadowElevation = 7.dp) {
                SimpleCalendarTitle(
                    modifier = Modifier
                        .background(toolbarColor)
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    currentMonth = visibleMonth.yearMonth,
                )
            }
            HorizontalCalendar(
                modifier = Modifier.wrapContentWidth(),
                state = state,
                dayContent = { day ->
                    CompositionLocalProvider(LocalRippleTheme provides Example3RippleTheme) {
//                        val colors = if (day.position == DayPosition.MonthDate) {
//                            flights[day.date].orEmpty().map { colorResource(it.color) }
//                        } else {
//                            emptyList()
//                        }
                        val events = if(reservationList[day.date] != null){
                            reservationList[day.date]!!.count()
                        } else {
                            0
                        }
                        Day(
                            day = day,
                            isSelected = selection == day,
                            events = events,
                            colors = colors
                        ) { clicked ->
                            selection = clicked
                        }
                    }
                },
                monthHeader = {
                    MonthHeader(
                        modifier = Modifier.padding(vertical = 8.dp),
                        daysOfWeek = daysOfWeek,
                    )
                },
            )
            Divider(color = pageBackgroundColor)
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = reservationsInSelectedDate.value) { flight ->
                    FlightInformation(flight)
                }
            }
        }
    }
}

@Composable
private fun Day( //small day cell in calendar
    day: CalendarDay,
    isSelected: Boolean = false,
    events: Int,
    colors: List<Color>,
    onClick: (CalendarDay) -> Unit = {},
) {
    Surface(shape = MaterialTheme.shapes.small, modifier = Modifier.padding(2.dp)) {
        Box(
            modifier = Modifier
                .aspectRatio(1f) // This is important for square-sizing!
                .border(
                    width = if (isSelected) 1.dp else 0.dp,
                    color = if (isSelected) selectedItemColor else Color.Transparent,
                    shape = MaterialTheme.shapes.small
                )
                .background(color = itemBackgroundColor)
                // Disable clicks on inDates/outDates
                .clickable(
                    enabled = day.position == DayPosition.MonthDate,
                    onClick = { onClick(day) },
                ),
        ) {
            val textColor = when (day.position) {
                DayPosition.MonthDate -> Color.Unspecified
                DayPosition.InDate, DayPosition.OutDate -> inActiveTextColor
            }
            Text( //day number text
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 3.dp, end = 4.dp),
                text = day.date.dayOfMonth.toString(),
                color = textColor,
                fontSize = 12.sp,
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 0.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                //for (event in colors.subList(0, min(colors.size, 3))) {
                for(e in 0 until events){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .background(colors[1]),
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek> = emptyList(),
) {
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground,
                text = dayOfWeek.displayText(uppercase = true),
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
private fun LazyItemScope.FlightInformation(reservation: Reservation) {
    Row(
        modifier = Modifier
            .fillParentMaxWidth()
            .height(IntrinsicSize.Max),
    ) {
        Surface(shape = MaterialTheme.shapes.small, modifier = Modifier.padding(2.dp)) {
            Box(
                modifier = Modifier
                    .background(color = colorResource(R.color.teal_700))
                    .fillParentMaxWidth(1 / 7f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = reservation.slot,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
        Surface(shape = MaterialTheme.shapes.small, modifier = Modifier.padding(2.dp)) {
            Box(
                modifier = Modifier
                    .background(color = itemBackgroundColor)
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                ){
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = reservation.date,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                    )
                }
                //AirportInformation(flight.departure, isDeparture = true)
            }
        }
    }
    Divider(color = pageBackgroundColor, thickness = 2.dp)
}

@Composable
fun SimpleCalendarTitle(
    modifier: Modifier,
    currentMonth: YearMonth,
) {
    Row(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column{
            Text(
                modifier = Modifier
                    .weight(1f)
                    .testTag("YearTitle")
                    .padding(horizontal = 8.dp),
                text = currentMonth.displayYear(),
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Light,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .testTag("MonthTitle")
                    .padding(horizontal = 8.dp),
                text = currentMonth.month.displayText(short = false),
                fontSize = 32.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

// The default dark them ripple is too bright so we tone it down.
private object Example3RippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(Color.Gray, lightTheme = false)

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(Color.Gray, lightTheme = false)
}

fun YearMonth.displayYear(): String {
    return "${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    // Only take non-null values as null will be produced when the
    // list is mid-scroll as no index will be completely visible.
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.completelyVisibleMonths.firstOrNull() }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

private val CalendarLayoutInfo.completelyVisibleMonths: List<CalendarMonth>
    get() {
        val visibleItemsInfo = this.visibleMonthsInfo.toMutableList()
        return if (visibleItemsInfo.isEmpty()) {
            emptyList()
        } else {
            val lastItem = visibleItemsInfo.last()
            val viewportSize = this.viewportEndOffset + this.viewportStartOffset
            if (lastItem.offset + lastItem.size > viewportSize) {
                visibleItemsInfo.removeLast()
            }
            val firstItem = visibleItemsInfo.firstOrNull()
            if (firstItem != null && firstItem.offset < this.viewportStartOffset) {
                visibleItemsInfo.removeFirst()
            }
            visibleItemsInfo.map { it.month }
        }
    }
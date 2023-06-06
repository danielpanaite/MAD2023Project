package com.example.courtreservationapplicationjetpack.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.firestore.Court
import com.example.courtreservationapplicationjetpack.firestore.CourtViewModel
import com.example.courtreservationapplicationjetpack.firestore.Reservation
import com.example.courtreservationapplicationjetpack.firestore.ReservationViewModel
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.firestore.toDate
import com.example.courtreservationapplicationjetpack.firestore.toTime
import com.example.courtreservationapplicationjetpack.models.sport.SportDrawables
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
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
    onReservationClick: (Reservation) -> Unit,
    viewModel: ReservationViewModel = viewModel(),
    courtViewModel: CourtViewModel = viewModel(),
    googleAuthUiClient : GoogleAuthUiClient,
) {
    val email = googleAuthUiClient.getSignedInUser()?.email
    if(email != null)
        viewModel.getUserReservations(email)
    val reservationFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val resList = viewModel.reservations.value
//        .filter { LocalDate.parse(it.toDate(), reservationFormatter) >= LocalDate.now() }
//        .filter { LocalTime.parse(it.toTime(), timeFormatter) > LocalTime.now() }
        .groupBy { LocalDate.parse(it.toDate(), reservationFormatter) }
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }
    val colors: List<Int> = listOf(R.color.blue_200, R.color.orange_200, R.color.green_200, R.color.red_200, R.color.cyan_200, R.color.yellow_200)
    val date = selection?.date
    val reservationsInSelectedDate =
        if (date == null) emptyList() else resList[date].orEmpty()
    LaunchedEffect(selection){
        if(reservationsInSelectedDate.isNotEmpty())
            courtViewModel.getReservationCourts(reservationsInSelectedDate.map{ it.court })
    }
    //main column that contains the whole page
    Column( modifier = Modifier
        .fillMaxSize()
        .background(pageBackgroundColor)) {
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
            Surface(shadowElevation = 12.dp) {
                SimpleCalendarTitle(
                    modifier = Modifier
                        .background(toolbarColor)
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    currentMonth = visibleMonth.yearMonth,
                )
            }
            HorizontalCalendar(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(12.dp),
                state = state,
                dayContent = { day ->
                    CompositionLocalProvider(LocalRippleTheme provides Example3RippleTheme) {
                        val events = if(resList[day.date] != null){
                            if(resList[day.date]!!.count() >= 3)
                                3
                            else
                                resList[day.date]!!.count()
                        } else {
                            0
                        }
                        Day(
                            day = day,
                            isSelected = selection == day,
                            events = events
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
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp), shadowElevation = 12.dp){
                //Bottom reservation details
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    if(viewModel.reservations.value.isEmpty()){
                        item {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                text = "No reservations",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSecondary,
                            )
                        }
                    }else {
                        if(reservationsInSelectedDate.isNotEmpty() && courtViewModel.reservationcourts.value.isNotEmpty() && (reservationsInSelectedDate.size == courtViewModel.reservationcourts.value.size) && email != null)
                            for( i in reservationsInSelectedDate.indices){
                                item{
                                    ReservationInformation(reservationsInSelectedDate[i], courtViewModel.reservationcourts.value.first{ it.id == reservationsInSelectedDate[i].court }, onReservationClick, colors, email)
                                }
                            }
                    }
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
                for(e in 0 until events){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .background(MaterialTheme.colorScheme.primary),
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
private fun LazyItemScope.ReservationInformation(
    reservation: Reservation,
    courtDetails: Court,
    onReservationClick: (Reservation) -> Unit,
    colors: List<Int>,
    email: String,
    userViewModel: UserViewModel = viewModel()
) {
    userViewModel.getUserByEmail(reservation.user)
    Row(
        modifier = Modifier
            .fillParentMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            .clickable {
                onReservationClick(reservation)
            }
    ) {
        Surface(shape = MaterialTheme.shapes.small, modifier = Modifier.padding(2.dp)) {
            Box(
                modifier = Modifier
                    .background(color = colorResource(colors[(1) % colors.size]))
                    .padding(bottom = if(reservation.invites.contains(email)) 8.dp else 0.dp)
                    .fillParentMaxWidth(1 / 7f)
                    .aspectRatio(1f)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    val sportIcon = SportDrawables.getDrawable(courtDetails.sport)
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp),
                        text = reservation.toTime(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Image(
                        painter = painterResource(sportIcon),
                        contentDescription = "Sport icon",
                        colorFilter = ColorFilter.tint(Color.Black),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        Surface(shape = MaterialTheme.shapes.small, modifier = Modifier.padding(2.dp)) {
            Column(
                modifier = Modifier
                    .background(color = itemBackgroundColor)
                    .fillMaxHeight(),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    text = courtDetails.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = courtDetails.address,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                )
                if(reservation.invites.contains(email) && userViewModel.user.value.id != "")
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        text = "invite from ${userViewModel.user.value.nickname}",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GreyItemInactive
                    )
            }
        }
    }
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

//BSMP
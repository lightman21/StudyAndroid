package org.ith.j2se.rxjava.chapter3;

import java.time.LocalDate;
import org.ith.j2se.rxjava.chapter3.entities.City;
import org.ith.j2se.rxjava.chapter3.entities.Vacation;
import org.ith.j2se.rxjava.chapter3.entities.Weather;
import rx.Observable;

/**
 * Created by tanghao on 12/6/16.
 *
 * suppose that you would like to plan a one-day vacation in some city.
 *
 * when the eather is sunny
 *
 * and airfare and hotels are cheap
 *
 * to do so,we will combine several streams together and com up with all possible results
 */
public class VacationDemo {
  public static void main(String[] args) {
    Observable<LocalDate> nextTenDays =
        Observable.range(1, 10)
            .map(i -> LocalDate.now().plusDays(i));

    Observable<Vacation> possibleVacations = Observable
        .just(City.Warsaw, City.London, City.Paris)
        .flatMap(
            city -> nextTenDays.map(date -> new Vacation(city, date))
                .flatMap
                    (vacation ->
                        Observable.zip
                            (
                                vacation.weather().filter(Weather::isSunny),
                                vacation.cheapFlightFrom(City.NewYork),
                                vacation.cheapHotel(),
                                (w, f, h) -> vacation
                            )
                    )

        );

    possibleVacations.forEach(System.out::println);
  }
}

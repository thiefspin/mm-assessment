package com.thiefspin.javasolution.service.impl;

import com.thiefspin.javasolution.entity.Country;
import com.thiefspin.javasolution.entity.Session;
import com.thiefspin.javasolution.models.Menu;
import com.thiefspin.javasolution.models.UssdRequest;
import com.thiefspin.javasolution.models.UssdResponse;
import com.thiefspin.javasolution.repository.CountryRepository;
import com.thiefspin.javasolution.repository.SessionRepository;
import com.thiefspin.javasolution.service.SessionService;
import com.thiefspin.javasolution.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionRepository repository;

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Optional<UssdResponse> handleRequest(UssdRequest req) {
        if (userExists(req.getMsisdn())) {
            if (req.getUserEntry() == null) {
                return menu1Flow(req.getSessionId());
            }
            Optional<Session> session = repository.findBySessionIdAndCompleted(req.getSessionId(), false);
            if (!session.isPresent()) {
                return menu2Flow(req);
            }
            if (session.get().getCurrentMenu().equals(Menu.MENU2.name())) {
                return complete(session.get());
            }
            return menu3Flow(session.get(), req.getUserEntry());
        }
        return Optional.empty();
    }

    private Optional<UssdResponse> complete(Session session) {
        repository.save(session.complete());
        return Optional.of(new UssdResponse(session.getSessionId(), Menu.MENU4.getMessage()));
    }

    private Optional<UssdResponse> menu3Flow(Session session, String userEntry) {
        repository.save(
                session.setAmount(Double.parseDouble(userEntry))
                        .setMenu(Menu.MENU2.name())
        );
        Optional<Country> country = countryRepository.findById(session.getCountryId());
        String message = Menu.MENU3.getMessage().replace("#placeholder", getMenu3Options(session.getAmount(), country.get()));
        return Optional.of(new UssdResponse(session.getSessionId(), message));
    }

    private Optional<UssdResponse> menu2Flow(UssdRequest req) {
        Optional<Country> country = countryRepository.findById(Long.parseLong(req.getUserEntry()));
        if (country.isPresent()) {
            Session session = repository.save(
                    new Session(req.getSessionId(),
                            req.getMsisdn(),
                            Menu.MENU1.name(),
                            country.get().getId()
                    )
            );
            String message = Menu.MENU2.getMessage().replace("#placeholder", country.get().getName());
            return Optional.of(new UssdResponse(session.getSessionId(), message));
        }
        return Optional.empty();
    }

    private Optional<UssdResponse> menu1Flow(String sessionId) {
        String message = Menu.MENU1.getMessage().replace("#placeholder", getCountriesUssdOptions());
        return Optional.of(new UssdResponse(sessionId, message));
    }

    private String getMenu3Options(Double randValue, Country country) {
        return calculateCurrencyConversion(randValue, country.getCurrencyValue()) +
                country.getCurrencyCode();
    }

    private Double calculateCurrencyConversion(Double randValue, Double conversionRate) {
        return randValue * conversionRate;
    }

    private String getCountriesUssdOptions() {
        StringBuilder builder = new StringBuilder();
        countryRepository.findAll().forEach(country -> {
            builder.append("\n")
                    .append(country.getId())
                    .append(")")
                    .append(country.getName());
        });
        return builder.toString();
    }

    private boolean userExists(String msisdn) {
        return userService.exists(msisdn);
    }
}

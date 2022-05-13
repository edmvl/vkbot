package ru.zhendozzz.vkbot.service;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import ru.zhendozzz.vkbot.dao.entity.Horo;
import ru.zhendozzz.vkbot.dao.repository.HoroRepository;
import ru.zhendozzz.vkbot.enums.HoroscopeEnum;
import ru.zhendozzz.vkbot.enums.JobType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
public class HoroService {

    private final HoroRepository horoRepository;
    private final JobLogService jobLogService;

    public HoroService(HoroRepository horoRepository, JobLogService jobLogService) {
        this.horoRepository = horoRepository;
        this.jobLogService = jobLogService;
    }

    public String grubDataFromResource() {
        LocalDate now = LocalDate.now();
        if (jobLogService.isGroupNotProcessed(null, now, JobType.HORO_LOAD.getSysName())) {
            Arrays.stream(HoroscopeEnum.values())
                    .map(sign -> parseHoro(sign.getSysname()))
                    .forEach(data -> horoRepository.save(Horo.builder()
                            .date(now)
                            .sign(data.getLeft())
                            .text(data.getRight())
                            .build()));
            jobLogService.saveLog(null, now, "Ok", true, JobType.HORO_LOAD.getSysName());
            return "Ok";
        } else {
            jobLogService.saveLog(null, now, "Diplicate", false, JobType.HORO_LOAD.getSysName());
            return "Гороскопы уже загружены ранее";
        }
    }

    public String getHoroToDate(LocalDate date) {
        if (jobLogService.isGroupNotProcessed(null, date, JobType.HORO_LOAD.getSysName())) {
            grubDataFromResource();
        }
        HoroscopeEnum[] values = HoroscopeEnum.values();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("\n")
                .append("Гороскоп на сегодня")
                .append("\n")
                .append("---------------------")
                .append("\n");
        for (HoroscopeEnum value : values) {
            Optional<Horo> horoByDateAndSign = getHoroByDateAndSign(date, value.getSysname());
            if (horoByDateAndSign.isPresent()) {
                Horo horo = horoByDateAndSign.get();
                stringBuilder
                        .append(HoroscopeEnum.bySysname(horo.getSign()).getName())
                        .append("\n")
                        .append(horo.getText())
                        .append("\n")
                        .append("--------------------")
                        .append("\n");
            }
        }
        return stringBuilder.toString();
    }

    private Optional<Horo> getHoroByDateAndSign(LocalDate date, String sysname) {
        return horoRepository.getAllByDateAndSign(date, sysname);
    }

    @SneakyThrows
    private Pair<String, String> parseHoro(String sign) {
        String url = "https://horo.mail.ru/prediction/" + sign + "/today/";
        Document document = getHTMLPage(url);
        while (Objects.isNull(document)) {
            Thread.sleep(10000);
            document = getHTMLPage(url);
        }
        String text = document.body().select(".article__item").text();
        return Pair.of(sign, text);
    }

    private Document getHTMLPage(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .followRedirects(true)
                    .execute()
                    .parse();
        } catch (IOException e) {
            return null;
        }
    }
}

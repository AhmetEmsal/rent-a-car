package kodlama.io.rentacar.common.constants;

public class Regex {
    public final static String Plate = "^" + // https://tr.wikipedia.org/wiki/T%C3%BCrkiye_ta%C5%9F%C4%B1t_plaka_kodu#:~:text=Harf%20ve%20rakam%20gruplar%C4%B1%20%C5%9F%C3%B6yledir
            "(0[1-9]|[1-7][0-9]|8[0-1])" +
            "\\s" +
            "(" +
            "\\[A-Z]\\s\\d{4,5}|" +
            "\\[A-Z]{2}\\s\\d{3,4}|" +
            "\\[A-Z]{3}\\s\\d{2,3}" +
            ")$";
}

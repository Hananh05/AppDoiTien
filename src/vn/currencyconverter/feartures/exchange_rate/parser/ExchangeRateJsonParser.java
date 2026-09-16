package vn.currencyconverter.feartures.exchange_rate.parser;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import vn.currencyconverter.feartures.exchange_rate.model.ExchangeRate;

/** Reads the JSON response used by Vietcombank's exchange-rate page. */
public final class ExchangeRateJsonParser {
    public record Result(List<ExchangeRate> rates, String publishedAt) {}
    public static Result parse(String json) {
        Reader reader = new Reader(json);
        Object value = reader.value();
        reader.space();
        if (reader.pos != json.length() || !(value instanceof Map<?, ?> root))
            throw new IllegalArgumentException("Phản hồi JSON không hợp lệ");
        String published = text(root, "UpdatedDate");
        OffsetDateTime.parse(published);
        if (!(root.get("Data") instanceof List<?> rows)) throw new IllegalArgumentException("Thiếu Data");
        List<ExchangeRate> rates = new ArrayList<>();
        Set<String> codes = new HashSet<>();
        for (Object row : rows) {
            if (!(row instanceof Map<?, ?> item)) throw new IllegalArgumentException("Dòng tỷ giá không hợp lệ");
            String code = text(item, "currencyCode");
            if (!code.matches("[A-Z]{3}") || !codes.add(code)) throw new IllegalArgumentException("Mã ngoại tệ không hợp lệ");
            var rate = new ExchangeRate(code, text(item, "currencyName"), number(item, "cash"), number(item, "sell"));
            rate.setTransferRate(number(item, "transfer"));
            rates.add(rate);
        }
        if (rates.isEmpty()) throw new IllegalArgumentException("Bảng tỷ giá trống");
        return new Result(List.copyOf(rates), published);
    }
    private static String text(Map<?, ?> map, String key) {
        if (!(map.get(key) instanceof String s) || s.isBlank()) throw new IllegalArgumentException("Thiếu " + key);
        return s;
    }
    private static BigDecimal number(Map<?, ?> map, String key) {
        if (!map.containsKey(key)) throw new IllegalArgumentException("Thiếu " + key);
        Object raw = map.get(key);
        if (raw == null || raw.toString().isBlank() || raw.equals("-")) return null;
        BigDecimal n = new BigDecimal(raw.toString());
        if (n.signum() < 0) throw new IllegalArgumentException("Tỷ giá âm");
        return n.signum() == 0 ? null : n;
    }
    // Small dependency-free JSON reader for a plain javac project.
    private static final class Reader {
        final String s; int pos; int depth;
        Reader(String s) { this.s = s; }
        void space() { while (pos < s.length() && " \r\n\t".indexOf(s.charAt(pos)) >= 0) pos++; }
        boolean take(char c) { space(); if (pos < s.length() && s.charAt(pos)==c) { pos++; return true; } return false; }
        void need(char c) { if (!take(c)) throw new IllegalArgumentException("JSON: expected " + c); }
        Object value() {
            space();
            if (++depth > 32) throw new IllegalArgumentException("JSON quá sâu");
            try {
                if (pos >= s.length()) throw new IllegalArgumentException("JSON bị thiếu");
                char c=s.charAt(pos);
                if(c=='"') return string();
                if(take('{')) {
                    Map<String,Object> map=new LinkedHashMap<>();
                    if(take('}')) return map;
                    do { String key=string(); need(':'); if(map.containsKey(key)) throw new IllegalArgumentException("Trùng khóa JSON"); map.put(key,value()); } while(take(','));
                    need('}'); return map;
                }
                if(take('[')) {
                    List<Object> list=new ArrayList<>();
                    if(take(']')) return list;
                    do { list.add(value()); } while(take(',')); need(']'); return list;
                }
                for(String literal:List.of("null","true","false")) if(s.startsWith(literal,pos)) { pos+=literal.length(); return literal.equals("null")?null:Boolean.valueOf(literal); }
                int start=pos;
                while(pos<s.length() && "-+0123456789.eE".indexOf(s.charAt(pos))>=0) pos++;
                String number=s.substring(start,pos);
                if(!number.matches("-?(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][+-]?[0-9]+)?")) throw new IllegalArgumentException("JSON number");
                return new BigDecimal(number);
            } finally { depth--; }
        }
        String string() {
            need('"'); StringBuilder out=new StringBuilder();
            while(pos<s.length()) {
                char c=s.charAt(pos++);
                if(c=='"') return out.toString();
                if(c<32) throw new IllegalArgumentException("JSON control character");
                if(c!='\\') { out.append(c); continue; }
                if(pos>=s.length()) break;
                c=s.charAt(pos++);
                switch(c) {
                    case '"','\\','/' -> out.append(c);
                    case 'b' -> out.append('\b'); case 'f' -> out.append('\f');
                    case 'n' -> out.append('\n'); case 'r' -> out.append('\r'); case 't' -> out.append('\t');
                    case 'u' -> { if(pos+4>s.length()) throw new IllegalArgumentException("JSON unicode"); out.append((char)Integer.parseInt(s.substring(pos,pos+4),16)); pos+=4; }
                    default -> throw new IllegalArgumentException("JSON escape");
                }
            }
            throw new IllegalArgumentException("JSON string bị thiếu");
        }
    }
}

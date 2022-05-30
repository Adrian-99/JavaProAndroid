package pl.adrian99.javaproandroid.data.dtos;

import java.util.List;

public class CodeExample {

    private String name;
    private String context;
    private List<Code> codes;

    public String getName() {
        return name;
    }

    public String getContext() {
        return context;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }
}

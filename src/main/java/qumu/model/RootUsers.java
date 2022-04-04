package qumu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class RootUsers {
    public int page;
    public int per_page;
    public int total;
    public int total_pages;
    public ArrayList<DatumUsers> data;
    public Support support;
}
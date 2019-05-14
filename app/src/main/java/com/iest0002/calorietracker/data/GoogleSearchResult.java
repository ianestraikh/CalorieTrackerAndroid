package com.iest0002.calorietracker.data;

import java.util.List;

public class GoogleSearchResult {
    private List<Item> items;

    public GoogleSearchResult(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private String snippet;
        private Pagemap pagemap;

        public Item(String snippet, Pagemap pagemap) {
            this.snippet = snippet;
            this.pagemap = pagemap;
        }

        public String getSnippet() {
            return snippet;
        }

        public void setSnippet(String snippet) {
            this.snippet = snippet;
        }

        public Pagemap getPagemap() {
            return pagemap;
        }

        public void setPagemap(Pagemap pagemap) {
            this.pagemap = pagemap;
        }
    }

    public static class Pagemap {
        private List<Src> cseImage;

        public Pagemap(List<Src> cseImage) {
            this.cseImage = cseImage;
        }

        public List<Src> getCseImage() {
            return cseImage;
        }

        public void setCseImage(List<Src> cseImage) {
            this.cseImage = cseImage;
        }
    }

    public static class Src {
        private String src;

        public Src(String src) {
            this.src = src;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }
    }
}

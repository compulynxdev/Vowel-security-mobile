package com.evisitor.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlaggedVisitorResponse {

    /**
     * content : [{"reason":"Guest has forgotten something","lastModifiedDate":"2020-09-08T05:38:14Z","documentType":null,"lastModifiedBy":"account","profile":"EXPECTED_VISITOR","fullName":"HIMESH","documentName":null,"type":"GUEST","createdDate":"2020-09-08T05:38:14Z","createdBy":"account","id":5,"guestId":9,"contactNo":"8565556"}]
     * pageable : {"sort":{"sorted":true,"unsorted":false,"empty":false},"pageNumber":0,"pageSize":10,"offset":0,"paged":true,"unpaged":false}
     * totalElements : 1
     * totalPages : 1
     * last : true
     * first : true
     * numberOfElements : 1
     * sort : {"sorted":true,"unsorted":false,"empty":false}
     * size : 10
     * number : 0
     * empty : false
     */

    private PageableBean pageable;
    private int totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private int numberOfElements;
    private SortBeanX sort;
    private int size;
    private int number;
    private boolean empty;
    private List<ContentBean> content;

    public PageableBean getPageable() {
        return pageable;
    }

    public void setPageable(PageableBean pageable) {
        this.pageable = pageable;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public SortBeanX getSort() {
        return sort;
    }

    public void setSort(SortBeanX sort) {
        this.sort = sort;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class PageableBean {
        /**
         * sort : {"sorted":true,"unsorted":false,"empty":false}
         * pageNumber : 0
         * pageSize : 10
         * offset : 0
         * paged : true
         * unpaged : false
         */

        private SortBean sort;
        private int pageNumber;
        private int pageSize;
        private int offset;
        private boolean paged;
        private boolean unpaged;

        public SortBean getSort() {
            return sort;
        }

        public void setSort(SortBean sort) {
            this.sort = sort;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public boolean isPaged() {
            return paged;
        }

        public void setPaged(boolean paged) {
            this.paged = paged;
        }

        public boolean isUnpaged() {
            return unpaged;
        }

        public void setUnpaged(boolean unpaged) {
            this.unpaged = unpaged;
        }

        public static class SortBean {
            /**
             * sorted : true
             * unsorted : false
             * empty : false
             */

            private boolean sorted;
            private boolean unsorted;
            private boolean empty;

            public boolean isSorted() {
                return sorted;
            }

            public void setSorted(boolean sorted) {
                this.sorted = sorted;
            }

            public boolean isUnsorted() {
                return unsorted;
            }

            public void setUnsorted(boolean unsorted) {
                this.unsorted = unsorted;
            }

            public boolean isEmpty() {
                return empty;
            }

            public void setEmpty(boolean empty) {
                this.empty = empty;
            }
        }
    }

    public static class SortBeanX {
        /**
         * sorted : true
         * unsorted : false
         * empty : false
         */

        private boolean sorted;
        private boolean unsorted;
        private boolean empty;

        public boolean isSorted() {
            return sorted;
        }

        public void setSorted(boolean sorted) {
            this.sorted = sorted;
        }

        public boolean isUnsorted() {
            return unsorted;
        }

        public void setUnsorted(boolean unsorted) {
            this.unsorted = unsorted;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }
    }

    public static class ContentBean {
        /**
         * reason : Guest has forgotten something
         * lastModifiedDate : 2020-09-08T05:38:14Z
         * documentType : null
         * lastModifiedBy : account
         * profile : EXPECTED_VISITOR
         * fullName : HIMESH
         * documentName : null
         * type : GUEST
         * createdDate : 2020-09-08T05:38:14Z
         * createdBy : account
         * id : 5
         * guestId : 9
         * contactNo : 8565556
         */

        private String reason;
        private String lastModifiedDate;
        private String documentType;
        private String lastModifiedBy;
        private String profile;
        private String fullName;
        private Object documentName;
        private String type;
        private String createdDate;
        private String createdBy;
        private int id;
        private int guestId;
        private String contactNo;
        private String dialingCode;
        @SerializedName("image")
        private String imageUrl;

        public String getReason() {
            return reason == null ? "" : reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getLastModifiedDate() {
            return lastModifiedDate == null ? "" : lastModifiedDate;
        }

        public void setLastModifiedDate(String lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

        public String getDocumentType() {
            return documentType == null ? "" : documentType;
        }

        public void setDocumentType(String documentType) {
            this.documentType = documentType;
        }

        public String getLastModifiedBy() {
            return lastModifiedBy == null ? "" : lastModifiedBy;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

        public String getProfile() {
            return profile == null ? "" : profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getFullName() {
            return fullName == null ? "" : fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Object getDocumentName() {
            return documentName;
        }

        public void setDocumentName(Object documentName) {
            this.documentName = documentName;
        }

        public String getType() {
            return type == null ? "" : type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getCreatedBy() {
            return createdBy == null ? "" : createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGuestId() {
            return guestId;
        }

        public void setGuestId(int guestId) {
            this.guestId = guestId;
        }

        public String getContactNo() {
            return contactNo == null ? "" : contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getImageUrl() {
            return imageUrl == null ? "" : imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getDialingCode() {
            return dialingCode == null ? "" : dialingCode;
        }

        public void setDialingCode(String dialingCode) {
            this.dialingCode = dialingCode;
        }
    }
}

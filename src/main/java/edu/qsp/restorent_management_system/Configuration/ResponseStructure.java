package edu.qsp.restorent_management_system.Configuration;

/**
 * A generic class to represent the structure of API responses.
 * 
 * @param <T> the type of the data being returned in the response
 */
public class ResponseStructure<T> {

    /**
     * HTTP status code for the response.
     * Common values:
     * - 200: OK
     * - 400: Bad Request
     * - 404: Not Found
     * - 500: Internal Server Error
     */
    private int statusCode;

    /**
     * A message providing additional information about the response.
     * This could be a success message, error description, or other relevant details.
     */
    private String message;

    /**
     * The actual data being returned in the response.
     * The type of data is generic and defined by the type parameter <T>.
     */
    private T data;

    /**
     * Gets the HTTP status code for the response.
     * 
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the HTTP status code for the response.
     * 
     * @param statusCode the status code to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets the message providing additional information about the response.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message providing additional information about the response.
     * 
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the actual data being returned in the response.
     * 
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the actual data being returned in the response.
     * 
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }
}

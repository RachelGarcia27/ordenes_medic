package com.racheldev.orders.type.exception;

/**
 * Excepciones relacionadas con el manejo de errores a ser reportados al
 * usuario. Contiene una serie de constantes que hacen referencia a mensajes
 * predefinidos.
 */
public class ModelException extends Exception {

	private static final long serialVersionUID = 1L;

	// Lista de Mensajes con Localización para errores.
    /**
     * Menaje que indica que se creó el registro
     */
    public static final String FEEDBACK_RECORD_CREATED = "feedback.message.record.created";

    /**
     * Menaje que indica que se borró el registro
     */
    public static final String FEEDBACK_RECORD_DELETED = "feedback.message.record.deleted";

    /**
     * Menaje que indica que se editó el registro
     */
    public static final String FEEDBACK_RECORD_EDITED = "feedback.message.record.edited";

    /**
     * Menaje que indica que no se salvó el registro
     */
    public static final String ERROR_RECORD_NOT_SAVED = "error.message.not.saved";

    /**
     * Menaje que indica que no se encontró el registro
     */
    public static final String ERROR_RECORD_NOT_FOUND = "error.message.not.found";

    /**
     * Menaje que indica que no se actualizó el registro
     */
    public static final String ERROR_RECORD_NOT_UPDATED = "error.message.not.updated";

    /**
     * Menaje que indica que la hora no tiene un formato valido
     */
    public static final String ERROR_RECORD_NOT_HOUR = "error.message.not.hour";

    /**
     * Constructor por defecto de la clase.
     *
     * @param message Mensaje de retroalimentación para el usuario.
     */
    public ModelException(String message) {
        super(message);
    }

}

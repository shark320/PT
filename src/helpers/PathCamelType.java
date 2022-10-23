package helpers;

import model.CamelType;

/**
 * Record fitting CamelType to pass the Path and specifies minimal Speed
 *
 * @param camelType fitting camelType for the Path
 * @param path      path to pass
 * @param minSpeed  minimum possible speed to pass
 */
public record PathCamelType(CamelType camelType, Path path, double minSpeed) {

}

package helpers;

import model.Camel;

/**
 * Record represents pair Camel - Path
 *
 * @param path  path to pass
 * @param camel camel to pass the path
 */
public record PathCamel(Path path, Camel camel) {

}

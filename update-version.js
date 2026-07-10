const fs = require('fs');

module.exports = {
    prepare: async (pluginConfig, context) => {
        const { nextRelease, logger } = context;
        logger.log(`Updating mod version to ${nextRelease.version}`);

        try {
            const gradleFilePath = './gradle.properties';
            let contents = fs.readFileSync(gradleFilePath, 'utf8');
            contents = contents.replace(/^mod_version = .*/gm, `mod_version = ${nextRelease.version}`);
            fs.writeFileSync(gradleFilePath, contents, 'utf8');
        } catch (err) {
            console.error('Error updating gradle.properties:', err);
        }
    },
}
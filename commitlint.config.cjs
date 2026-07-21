const { RuleConfigSeverity } = require('@commitlint/types');

module.exports = {
  extends: [
    '@commitlint/config-conventional',
  ],
  rules: {
    'type-enum': [
      RuleConfigSeverity.Error,
      'always',
      [
        'feat',
        'fix',
        'perf',
        'revert',
        'tweak',
        'docs',
        'chore',
        'refactor',
        'test',
        'ci',
        'build',
      ],
    ],
  },
}
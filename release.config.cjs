const mcVersion = '1.20.1';

/**
 * @type {import('semantic-release').GlobalConfig}
 */
module.exports = {
  branches: [`${mcVersion}/main`],
  tagFormat: `${mcVersion}-v\${version}`,
  plugins: [
    [
      '@semantic-release/commit-analyzer',
      {
        preset: 'angular',
        releaseRules: [
          { type: 'tweak', release: 'patch' }
        ]
      }
    ],
    // --------------------
    [
      '@semantic-release/release-notes-generator',
      {
        preset: 'conventionalcommits',
        presetConfig: {
          types: [
            { type: 'feat', section: '✨ Features' },
            { type: 'fix', section: '🐛 Bug Fixes' },
            { type: 'perf', section: '⚡ Performance Improvements' },
            { type: 'revert', section: '↩️ Reverts' },
            { type: 'tweak', section: '⚙️ Tweaks', hidden: false },
            { type: 'docs', section: '📝 Documentation', hidden: true },
            { type: 'chore', section: '🧹 Miscellaneous Chores', hidden: true },
            { type: 'refactor', section: '🪄 Code Refactoring', hidden: true },
            { type: 'test', section: '✅ Tests', hidden: true },
            { type: 'ci', section: '🔁 Continuous Integration', hidden: true },
          ],
        },
      },
    ],
    // --------------------
    [
      '@semantic-release/changelog',
      {
        changelogFile: 'CHANGELOG.md',
      },
    ],
    // --------------------
    './update-version.js',
    // --------------------
    [
      '@semantic-release/exec',
      {
        prepareCmd: './gradlew build --build-cache',
        successCmd: `git fetch origin ${mcVersion}/dev ${mcVersion}/main && git checkout ${mcVersion}/dev && git merge origin/${mcVersion}/main && git push origin ${mcVersion}/dev`,
      },
    ],
    // --------------------
    [
      '@semantic-release/github',
      {
        assets: [
          'fabric/build/libs/!(*-@(dev-shadow|sources)).jar',
          'forge/build/libs/!(*-@(dev-shadow|sources)).jar',
        ],
      },
    ],
    // --------------------
    [
      '@semantic-release/git',
      {
        assets: [
          'gradle.properties',
        ],
        message: 'chore(release): ${nextRelease.version} [skip ci]',
      },
    ],
    // --------------------
    'semantic-release-export-data',
  ],
};

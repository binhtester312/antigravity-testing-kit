/**
 * logger.ts — Structured logging utility
 *
 * Cung cấp logging có cấu trúc với timestamp và log level.
 * KHÔNG dùng console.log() trong code — dùng Logger thay thế.
 */

export type LogLevel = 'DEBUG' | 'INFO' | 'WARN' | 'ERROR';

export class Logger {

  private readonly context: string;
  private readonly logLevel: LogLevel;

  private readonly levels: Record<LogLevel, number> = {
    DEBUG: 0,
    INFO: 1,
    WARN: 2,
    ERROR: 3,
  };

  constructor(context: string = 'Test', logLevel: LogLevel = 'INFO') {
    this.context = context;
    this.logLevel = (process.env['LOG_LEVEL'] as LogLevel) ?? logLevel;
  }

  private shouldLog(level: LogLevel): boolean {
    return this.levels[level] >= this.levels[this.logLevel];
  }

  private format(level: LogLevel, message: string): string {
    const timestamp = new Date().toISOString();
    return `[${timestamp}] [${level}] [${this.context}] ${message}`;
  }

  debug(message: string, data?: unknown): void {
    if (this.shouldLog('DEBUG')) {
      const formatted = this.format('DEBUG', message);
      process.stdout.write(formatted + (data ? ` | Data: ${JSON.stringify(data)}` : '') + '\n');
    }
  }

  info(message: string, data?: unknown): void {
    if (this.shouldLog('INFO')) {
      const formatted = this.format('INFO', message);
      process.stdout.write(formatted + (data ? ` | Data: ${JSON.stringify(data)}` : '') + '\n');
    }
  }

  warn(message: string, data?: unknown): void {
    if (this.shouldLog('WARN')) {
      const formatted = this.format('WARN', message);
      process.stderr.write(formatted + (data ? ` | Data: ${JSON.stringify(data)}` : '') + '\n');
    }
  }

  error(message: string, error?: unknown): void {
    if (this.shouldLog('ERROR')) {
      const formatted = this.format('ERROR', message);
      const errorDetail = error instanceof Error ? error.stack : JSON.stringify(error);
      process.stderr.write(formatted + (errorDetail ? ` | Error: ${errorDetail}` : '') + '\n');
    }
  }

  step(stepName: string): void {
    this.info(`▶ STEP: ${stepName}`);
  }

  action(action: string, locator: string): void {
    this.debug(`  → ${action} on [${locator}]`);
  }
}

/**
 * Factory function — tạo logger với context cụ thể
 * @example
 * const logger = createLogger('LoginPage');
 * logger.info('Clicking login button');
 */
export function createLogger(context: string): Logger {
  return new Logger(context);
}

// Default logger cho các helper functions
export const logger = createLogger('Framework');
